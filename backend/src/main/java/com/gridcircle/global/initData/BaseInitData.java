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

        Member member= memberService.join("admin@gmail.com", "admin", "1234", "서울시 강남구 역삼동", "ROLE_ADMIN");
        Member member1= memberService.join("admin1@gmail.com", "두효정", "1234", "서울시 강남구 역삼동","ROLE_USER");
        Member member2= memberService.join("admin2@gmail.com", "양현준", "1234", "서울시 강남구 역삼동", "ROLE_USER");
        Member member3= memberService.join("admin3@gmail.com", "이예진", "1234", "서울시 강남구 역삼동", "ROLE_USER");
        Member member4= memberService.join("admin4@gmail.com", "석희성", "1234", "서울시 강남구 역삼동", "ROLE_USER");
    }

    @Transactional
    public void makeProduct() {
        if (productService.count() > 0) return;

        Product product1 = productService.write("Natural Plants", "기본적인 원두 입니다.", "https://ifh.cc/g/xoVO1D.jpg|https://ifh.cc/g/HqQCN0.jpg|https://i.postimg.cc/zXFFRGFh/2.jpg|https://i.postimg.cc/d3TRtg2t/coffee-4591173-1920.jpg" , 5000, 20);
        Product product2 = productService.write("Premium Beans", "프리미엄 원두 입니다.", "https://i.postimg.cc/XqvW9tKf/coffee-3392159-1920.jpg|https://ifh.cc/g/CORDgm.jpg|https://i.postimg.cc/6qwG9CSm/3.jpg|https://i.postimg.cc/G2fJRbs5/coffee-5278346-1280.jpg", 10000, 15);
        Product product3 = productService.write("Special Blend", "스페셜 블렌딩 원두 입니다.", "https://i.postimg.cc/9QxPJgSF/1.jpg|https://i.postimg.cc/4dcz4f3m/2.jpg|https://i.postimg.cc/d3KGLb7n/1.jpg|https://i.postimg.cc/XqZLBrbW/farmer-6959620-1280.jpg", 15000, 10);
    }
}
