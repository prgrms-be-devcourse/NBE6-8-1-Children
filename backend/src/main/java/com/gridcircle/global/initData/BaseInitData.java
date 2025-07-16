package com.gridcircle.global.initData;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.entity.Product;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.member.member.service.ProductService;
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
    private final ProductService productService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.makeAdmin();
            self.makeProduct();
        };
    }

    @Transactional
    public void makeAdmin() {
        if(memberService.findByRole() > 0) return;

        Member member= memberService.join("admin", "관리자", "1234", "서울시 강남구 역삼동", "ROLE_ADMIN");
    }

    @Transactional
    public void makeProduct() {
        if (productService.count() > 0) return;

        Product product1 = productService.write("Natural Plants", "기본적인 원두 입니다.", "https://ifh.cc/g/HqQCN0.jpg" , 5000, 20);
        Product product2 = productService.write("Premium Beans", "프리미엄 원두 입니다.", "https://ifh.cc/g/CORDgm.jpg", 10000, 15);
        Product product3 = productService.write("Special Blend", "스페셜 블렌딩 원두 입니다.", "https://ifh.cc/g/xoVO1D.jpg", 15000, 10);
    }
}
