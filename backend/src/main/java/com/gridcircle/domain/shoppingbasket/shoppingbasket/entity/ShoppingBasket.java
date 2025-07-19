package com.gridcircle.domain.shoppingbasket.shoppingbasket.entity;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingBasket extends BaseEntity {
    private int productCount; // 담은 상품 갯수

    @ManyToOne
    private Member member; // FK

    @ManyToOne
    private Product product; // FK
}