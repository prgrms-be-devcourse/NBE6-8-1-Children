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
    private String productName;
    private int orderCount;
    private int productPrice;
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders; //FK

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; //FK
}
