package com.gridcircle.domain.shoppingbasket.shoppingbasket.dto;

public record ShoppingBasketResponseDto(
        String productName,
        String productImage,
        int productCount,
        int productPrice
){
}
