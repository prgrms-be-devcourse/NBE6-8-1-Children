package com.gridcircle.domain.order.order.dto;

import com.gridcircle.domain.order.order.entity.Order;
import com.gridcircle.domain.order.order.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto (
        int id,
        Long totalPrice,
        String address,
        String orderStatus,
        String deliveryStatus,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        //Long memberId,
        List<OrderItemResponseDto> orderItems
){
    public OrderResponseDto(Order order){
        this(
                order.getId(),
                order.getTotalPrice(),
                order.getAddress(),
                order.getOrderStatus().name(),
                order.isDeliveryStatus() ? "배송 시작" : "배송 시작 전",
                order.getCreatedDate(),
                order.getModifiedDate(),
                //order.getMember.getId(),
                order.getOrderItems().stream().map(OrderItemResponseDto::new).toList()
        );
    }

    public record OrderItemResponseDto(
            int id,
            //Long productId,
            String productName,
            int orderCount,
            int productPrice
    ){
        public OrderItemResponseDto(OrderItem item){
            this(
                    item.getId(),
                    //item.getProduct().getId(),
                    item.getProductName(),
                    item.getOrderCount(),
                    item.getProductPrice()
            );
        }
    }
}
