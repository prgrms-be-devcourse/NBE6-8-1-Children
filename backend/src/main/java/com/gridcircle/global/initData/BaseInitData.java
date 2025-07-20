package com.gridcircle.global.initData;

import com.gridcircle.domain.member.member.service.MemberService;
import com.gridcircle.domain.product.product.service.ProductService;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.service.ShoppingBasketService;
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
    @Autowired
    private ShoppingBasketService shoppingBasketService;



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

        if (memberService.findByEmail("admin@gmail.com").isEmpty())
            memberService.join("admin@gmail.com", "admin", "1234", "서울시 강남구 역삼동", "ADMIN");
        if (memberService.findByEmail("admin1@gmail.com").isEmpty())
            memberService.join("admin1@gmail.com", "두효정", "1234", "서울시 강남구 역삼동","USER");
        if (memberService.findByEmail("admin2@gmail.com").isEmpty())
            memberService.join("admin2@gmail.com", "양현준", "1234", "서울시 강남구 역삼동", "USER");
        if (memberService.findByEmail("admin3@gmail.com").isEmpty())
            memberService.join("admin3@gmail.com", "이예진", "1234", "서울시 강남구 역삼동", "USER");
        if (memberService.findByEmail("admin4@gmail.com").isEmpty())
            memberService.join("admin4@gmail.com", "석희성", "1234", "서울시 강남구 역삼동", "USER");
    }

    @Transactional
    public void makeProduct() {
        if (productService.count() > 0) return;

        productService.write("브라질 산토스 클래식", "부드럽고 고소한 맛이 특징인 브라질 산토스 원두를 사용했습니다.\\n데일리 커피로 부담 없이 즐길 수 있는 합리적인 선택!\\n누구나 편하게 마실 수 있는 기본 커피입니다.", "https://ifh.cc/g/xoVO1D.jpg|https://ifh.cc/g/HqQCN0.jpg|https://i.postimg.cc/zXFFRGFh/2.jpg|https://i.postimg.cc/d3TRtg2t/coffee-4591173-1920.jpg" , 5000, 20);
        productService.write("콜롬비아 수프리모 미디엄", "콜롬비아 수프리모 원두의 은은한 산미와 밸런스 잡힌 풍미를 느껴보세요.\\n깔끔한 바디감과 달콤한 향이 어우러져, 커피 본연의 맛을 즐기기에 딱 좋습니다.\\n일상 속 특별함을 더해주는 스테디셀러!", "https://i.postimg.cc/XqvW9tKf/coffee-3392159-1920.jpg|https://ifh.cc/g/CORDgm.jpg|https://i.postimg.cc/6qwG9CSm/3.jpg|https://i.postimg.cc/G2fJRbs5/coffee-5278346-1280.jpg", 10000, 15);
        productService.write("에티오피아 예가체프 프리미엄", "에티오피아 예가체프의 화사한 꽃향과 과일의 산뜻함이 살아있는 프리미엄 원두입니다.\\n깊고 복합적인 향미, 청량한 산미와 달콤한 여운이 남는 최고의 커피 경험을 선사합니다.\\n커피 애호가를 위한 고품격 스페셜티!", "https://i.postimg.cc/9QxPJgSF/1.jpg|https://i.postimg.cc/4dcz4f3m/2.jpg|https://i.postimg.cc/d3KGLb7n/1.jpg|https://i.postimg.cc/XqZLBrbW/farmer-6959620-1280.jpg", 15000, 10);
        productService.write("케냐 AA 스페셜", "케냐 AA 원두만의 진한 바디감과 상큼한 산미, 그리고 달콤한 과일향이 조화를 이룹니다.\\n한 모금마다 느껴지는 깊은 풍미와 깔끔한 피니시로 특별한 하루를 시작해보세요.\\n커피 마니아들에게 사랑받는 스페셜티 커피입니다.","https://i.postimg.cc/2yCwdzj8/coffee-6840457-1920.jpg|https://i.postimg.cc/4dcz4f3m/2.jpg|https://i.postimg.cc/d3KGLb7n/1.jpg|https://i.postimg.cc/XqZLBrbW/farmer-6959620-1280.jpg", 20000, 3);
        productService.write("과테말라 안티구아 스페셜", "카페인이 부담스러운 분들을 위한 부드러운 디카페인 커피!\\n과테말라 안티구아 원두의 고소함과 은은한 단맛을 그대로 담았습니다.\\n언제든 편하게 즐길 수 있는 건강한 커피입니다.","https://i.postimg.cc/x8gPtWqG/coffee-6840457-1920.jpg|https://i.postimg.cc/4dcz4f3m/2.jpg|https://i.postimg.cc/d3KGLb7n/1.jpg|https://i.postimg.cc/XqZLBrbW/farmer-6959620-1280.jpg", 30000, 1);
    }
}