package com.gridcircle.domain.shoppingbasket.shoppingbasket.dto;

public record ShoppingBasketResponseDto(
        int id, // 장바구니 PK
        String productName,
        String productImage,
        int productCount,
        int productPrice
){
}
