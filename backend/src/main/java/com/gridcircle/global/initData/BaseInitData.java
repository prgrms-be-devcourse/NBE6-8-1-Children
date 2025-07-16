package com.gridcircle.global.initData;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.product.product.service.ProductService;
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

        Product product1 = productService.write("Natural Plants", "기본적인 원두 입니다.", "" , 5000, 20);
        Product product2 = productService.write("Premium Beans", "프리미엄 원두 입니다.", "", 10000, 15);

        Product product3 = productService.write("Special Blend", "스페셜 블렌딩 원두 입니다.", "", 15000, 10);

        // 기본원두 https://ifh.cc/g/xoVO1D.jpg https://ifh.cc/v-HqQCN0 https://postimg.cc/tY7xKyfR
        // 프리미엄 원두 https://postimg.cc/CzKGwbZg https://ifh.cc/v-CORDgm
        // 스페셜 https://postimg.cc/9D7qfbCv https://postimg.cc/k6nVfdKm https://postimg.cc/HcBrNP97
        // 커피 수확 https://postimg.cc/yWpJXtNM 라떼 https://postimg.cc/VrGNg0zq
    }
}
