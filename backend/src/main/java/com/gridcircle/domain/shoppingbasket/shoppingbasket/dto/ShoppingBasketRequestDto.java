package com.gridcircle.domain.shoppingbasket.shoppingbasket.dto;

// 장바구니 등록을 위한 내용을 담은 DTO
public record ShoppingBasketRequestDto(
        int productId,
        int memberId, // 장바구니에 담는 회원의 ID
        int productCount
){
}
