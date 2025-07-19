package com.gridcircle.domain.shoppingbasket.shoppingbasket.entity;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingBasketItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // FK

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId; // FK

    private String productName; // 상품 이름
    private int productCount; // 상품 개수
    private int productPrice; // 상품 가격
    private String productImage; // 상품 이미지
}