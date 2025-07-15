package com.gridcircle.domain.order.order.entity;

import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
    private long totalPrice;

    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private boolean deliveryStatus;

    //@ManyToOne
    //private Member member;

    @OneToMany(mappedBy="order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderItem> orderItems = new ArrayList<>();
}
