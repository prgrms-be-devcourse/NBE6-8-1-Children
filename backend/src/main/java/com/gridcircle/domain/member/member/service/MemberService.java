package com.gridcircle.domain.member.member.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }


    public int findByRole() {
        return memberRepository.findAll().stream()
                .filter(member -> member.getRole().equals("ROLE_ADMIN"))
                .mapToInt(member -> 1)
                .sum();
    }


    public Member join(String email, String name, String password, String address, String role ) {
        memberRepository
                .findByEmail(email)
                .ifPresent(_member -> {
                    throw new ServiceException("409-1", "이미 존재하는 이메일입니다.");
                });

        password = passwordEncoder.encode(password);

        Member member = new Member( email, name, password, address, role);
        return memberRepository.save(member);
    }
}
