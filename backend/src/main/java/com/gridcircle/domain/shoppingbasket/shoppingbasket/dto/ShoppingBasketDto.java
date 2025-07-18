package com.gridcircle.domain.shoppingbasket.shoppingbasket.dto;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasket;

import java.time.LocalDateTime;

public record ShoppingBasketDto (
        int productCount,
        Member member,
        Product product,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate
){
    public ShoppingBasketDto(ShoppingBasket shoppingBasket) {
        this(
                shoppingBasket.getProductCount(),
                shoppingBasket.getMember(),
                shoppingBasket.getProduct(),
                shoppingBasket.getCreatedDate(),
                shoppingBasket.getModifiedDate()
        );
    }
}
