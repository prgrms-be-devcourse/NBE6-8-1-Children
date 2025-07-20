package com.gridcircle.domain.order.order.entity;

import com.gridcircle.domain.member.member.entity.Member;
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
public class Orders extends BaseEntity {
    private long totalPrice;

    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDERED;

    private boolean deliveryStatus = false;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; //FK

    @OneToMany(mappedBy="orders", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderItem> orderItems = new ArrayList<>();
}
