package com.gridcircle.domain.order.order.entity;

import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItem extends BaseEntity {
    private String productName; // 주문한 상품 이름
    private int orderCount; // 주문한 상품 개수
    private int productPrice; // 상품 가격

    @ManyToOne
    private Order order; 

    //@ManyToOne
    //private Product product;
}
