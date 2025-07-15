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
    private String productName;
    private int orderCount;
    private int productPrice;

    @ManyToOne
    private Order order;

    //@ManyToOne
    //private Product product;
}
