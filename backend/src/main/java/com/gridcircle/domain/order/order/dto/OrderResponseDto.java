package com.gridcircle.domain.order.order.dto;

import com.gridcircle.domain.order.order.entity.Orders;
import com.gridcircle.domain.order.order.entity.OrderItem;

import java.util.List;

// 주문 조회 페이지 GET 요청에 대한 응답 DTO
public record OrderResponseDto (
        int id,
        long totalPrice,
        String address,
        String orderStatus,
        boolean deliveryStatus,
        String createdDate,
        String modifiedDate,
        List<OrderItemResponseDto> orderItems
){
    public OrderResponseDto(Orders orders){
        this(
                orders.getId(),
                orders.getTotalPrice(),
                orders.getAddress(),
                orders.getOrderStatus().name(),  // enum을 String으로 변환해서 전달
                orders.isDeliveryStatus(),
                orders.getCreatedDate().toString(), // String으로 변환
                orders.getModifiedDate() != null ? orders.getModifiedDate().toString() : null, // null이 아니면 String으로 변환
                orders.getOrderItems().stream().map(orderItem -> new OrderItemResponseDto(orderItem)).toList()
        );
    }

    public record OrderItemResponseDto(
            int id, // 주문 내역에서 사용자가 주문 취소할 때 필요함
            int productId,
            String productName,
            int orderCount,
            int productPrice,
            String productImage
    ){
        public OrderItemResponseDto(OrderItem item){
            this(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getProductName(),
                    item.getOrderCount(),
                    item.getProductPrice(),
                    item.getProductImage()
            );
        }
    }
}
