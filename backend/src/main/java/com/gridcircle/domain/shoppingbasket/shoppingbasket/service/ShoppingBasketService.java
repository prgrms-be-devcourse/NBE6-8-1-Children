package com.gridcircle.domain.shoppingbasket.shoppingbasket.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.dto.ShoppingBasketDto;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.dto.ShoppingBasketResponseDto;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasket;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.repository.ShoppingBasketRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingBasketService {
    private final ShoppingBasketRepository shoppingBasketRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

//    // 상세페이지에 있는 데이터 + 사용자 주소를 장바구니로 보내는 dto 생성 (get)
//    @Transactional(readOnly = true)
//    public ShoppingBasketResponseDto getShoppingBasketData(int memberId) {
//        // 로그인 중인 member 조회
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(()->new IllegalArgumentException("로그인 또는 회원가입을 진행해주세요"));
//
//        // 현재 로그인중인 member의 상세페이지 정보를 모아서 객체에 저장
//        List<>
//    }

    // 상세페이지에서 장바구니 넣기 버튼을 눌렀을 때, 장바구니에 상품을 추가하는 메서드 (Post)
    @Transactional
    public ShoppingBasketDto createShoppingBasket(ShoppingBasketDto dto, int memberId) {
        // 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalIdentifierException("로그인 또는 회원가입을 해 주세요"));
        // 상품 정보 조회
        Product product = productRepository.findById(dto.product().getId())
                .orElseThrow(()->new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        // ShoppingBasket 엔티티 생성
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setMember(member);
        shoppingBasket.setProduct(product);
        shoppingBasket.setProductCount(dto.productCount());

        ShoppingBasket saved = shoppingBasketRepository.save(shoppingBasket);

        return new ShoppingBasketDto(saved);
    }

    // 장바구니 다건 조회 (get)
    @Transactional
    public List<ShoppingBasketDto> getShoppingBasket(int memberId) {
        // memberId로 장바구니 조회
        List<ShoppingBasket> shoppingBaskets = shoppingBasketRepository.findByMemberId(memberId);

        // 장바구니가 비어있으면 예외 처리
        if (shoppingBaskets.isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있습니다.");
        }

        return shoppingBaskets.stream()
                .map(ShoppingBasketDto::new) // ShoppingBasketDto로 변환
                .toList(); // 주문 리스트를 dto 리스트로 변환
    }

}
