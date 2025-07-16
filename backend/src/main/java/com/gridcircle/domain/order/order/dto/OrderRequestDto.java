package com.gridcircle.domain.order.order.dto;

import java.util.List;

// 프론트에서 주문 페이지 데이터 POST 요청이 왔을 때,
// 그 요청에 있는 데이터들을 받을 Dto
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
