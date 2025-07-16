package com.gridcircle.domain.member.member.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.global.standard.util.Ut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthTokenService {
    @Value("${custom.jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${custom.accessToken.expirationSeconds}")
    private int accessTokenExpirationSeconds;

    String genAccessToken(Member member) {
        long id = member.getId();
        String email = member.getEmail();
        String name = member.getName();

        return Ut.jwt.toString(
                jwtSecretKey,
                accessTokenExpirationSeconds,
                Map.of("id", id, "email", email, "name", name)
        );
    }

    Map<String, Object> payload(String accessToken) {
        Map<String, Object> parsedPayload = Ut.jwt.payload(jwtSecretKey, accessToken);

        if (parsedPayload == null) return null;

        int id = (int) parsedPayload.get("id");
        String username = (String) parsedPayload.get("username");
        String name = (String) parsedPayload.get("name");

        return Map.of("id", id, "username", username, "name", name);
    }
}