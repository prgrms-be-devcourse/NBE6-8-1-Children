package com.gridcircle.domain.order.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestDto (
        int totalPrice,
        String address,
        String orderStatus,
        boolean deliveryStatus,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        //Long memberId,
        List<OrderItemRequestDto> orderItems
){
    public record OrderItemRequestDto(
            Long productId,
            String productName,
            int orderCount,
            int productPrice
    ){}

}
