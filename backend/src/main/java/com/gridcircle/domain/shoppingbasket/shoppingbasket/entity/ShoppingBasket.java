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

    // JPA가 자동으로 member_id, product_id 컬럼을 생성합니다.
    // 명시적으로 @JoinColumn을 쓰는 것이 권장.
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // FK

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // FK
}