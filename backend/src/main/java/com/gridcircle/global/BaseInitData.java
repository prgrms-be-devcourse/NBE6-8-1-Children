package com.gridcircle.global;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    @Lazy
    @Autowired
    private BaseInitData self;
    private final MemberService memberService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.makeAdmin();
        };
    }

    @Transactional
    public void makeAdmin() {
        if(memberService.findByRole() > 0) return;

        Member member= memberService.join("admin", "관리자", "1234", "서울시 강남구 역삼동", "ROLE_ADMIN");
    }
}
