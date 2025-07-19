package com.gridcircle.domain.shoppingbasket.shoppingbasket.service;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.member.member.repository.MemberRepository;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.dto.ShoppingBasketRequestDto;
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

    // 상세페이지에서 장바구니 넣기 버튼을 눌렀을 때, 장바구니에 상품을 추가하는 메서드 (Post)
    @Transactional
    public void createShoppingBasket(ShoppingBasketRequestDto dto, int memberId) {
        // 사용자 정보 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalIdentifierException("로그인 또는 회원가입을 해 주세요"));

        // 상품 정보 조회
        Product product = productRepository.findById(dto.productId())
                .orElseThrow(()->new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        // ShoppingBasket 엔티티 생성
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setMember(member);
        shoppingBasket.setProduct(product);
        shoppingBasket.setProductCount(dto.productCount());

        shoppingBasketRepository.save(shoppingBasket);
    }

        // 장바구니에 담긴 아이템을 조회하는 메서드 (Get)
        @Transactional(readOnly=true)
        public List<ShoppingBasketResponseDto> getShoppingBasket(int memberId) {

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

            // 장바구니에 담긴 아이템 조회
            List<ShoppingBasket> baskets = shoppingBasketRepository.findByMemberId(memberId);

            // ShoppingBasket에서 productId로 Product를 조회해서 필요한 정보를 DTO에 담아줌
            return baskets.stream()
                    .map(basket -> {
                        // 각 장바구니 row의 productId로 Product 정보 조회
                        Product product = productRepository.findById(basket.getProduct().getId())
                                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
                        return new ShoppingBasketResponseDto(
                                product.getProductName(),
                                product.getProductImage(),
                                basket.getProductCount(),
                                product.getPrice()
                        );
                    })
                    .toList();
        }
}
