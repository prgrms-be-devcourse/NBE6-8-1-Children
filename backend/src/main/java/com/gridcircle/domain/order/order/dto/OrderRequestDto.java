package com.gridcircle.domain.order.order.dto;

import java.util.List;

// 주문 페이지 POST 요청에 있는 데이터들을 받는 DTO
public record OrderRequestDto (
        long totalPrice,
        String address,
        String orderStatus,
        boolean deliveryStatus,
        List<OrderItemRequestDto> orderItems
){
    public record OrderItemRequestDto(
            int productId,
            String productName,
            int orderCount,
            int productPrice,
            String productImage
    ){}
}
