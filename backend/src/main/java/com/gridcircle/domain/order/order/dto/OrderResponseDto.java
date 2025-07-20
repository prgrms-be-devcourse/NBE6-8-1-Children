package com.gridcircle.domain.order.order.dto;

import com.gridcircle.domain.order.order.entity.Orders;
import com.gridcircle.domain.order.order.entity.OrderItem;

import java.util.List;

// 주문 조회 페이지에서 프론트의 get 요청에 대한 응답으로 보내줄 Dto
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
                orders.getOrderStatus().name(),  // enum orderStatus를 String 타입으로 전달!
                orders.isDeliveryStatus(),
                orders.getCreatedDate().toString(), // createdDate가 LocalDateTime타입이니까, String으로 바꿈
                orders.getModifiedDate() != null ? orders.getModifiedDate().toString() : null,
                orders.getOrderItems().stream().map(OrderItemResponseDto::new).toList()
        );
    }

    public record OrderItemResponseDto(
            int id, // 주문 내역에서 사용자가 내역을 삭제할 때 필요함
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
