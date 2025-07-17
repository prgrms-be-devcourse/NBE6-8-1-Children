package com.gridcircle.domain.order.order.dto;

import java.util.List;

// 사용자가 장바구니에서 주문하기 버튼을 눌러 주문페이지로 이동했을 때,
// 주문페이지에 장바구니 내용 + 사용자 주소를 보여주기 위해 프론트로 전송할 데이터
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
