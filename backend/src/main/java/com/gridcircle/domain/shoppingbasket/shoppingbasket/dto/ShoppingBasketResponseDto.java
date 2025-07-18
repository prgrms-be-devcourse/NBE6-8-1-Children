package com.gridcircle.domain.shoppingbasket.shoppingbasket.dto;

import java.util.List;

public record ShoppingBasketResponseDto(
        List<ShoppingBasketItemDto> items
){
    public record ShoppingBasketItemDto(
            int productId,
            String productName,
            int productPrice,
            int orderCount,
            String productImage
    ){}
}
