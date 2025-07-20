package com.gridcircle.domain.order.order.entity;

import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
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
    private String productImage; // 상품 이미지

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //FK
}
