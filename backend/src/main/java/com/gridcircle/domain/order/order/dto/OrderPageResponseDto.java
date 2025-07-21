package com.gridcircle.domain.order.order.dto;

import java.util.List;

// 주문 페이지 GET 요청에 대한 응답 DTO
public record OrderPageResponseDto(
        String address,
        List<OrderPageItemDto> items
) {
    public record OrderPageItemDto(
            int productId,
            String productName,
            int productPrice,
            int orderCount,
            String productImage
    ){}

}
