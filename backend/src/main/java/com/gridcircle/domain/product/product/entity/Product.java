package com.gridcircle.domain.product.product.entity;

import com.gridcircle.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseEntity {
    private String productName; // 상품명
    private String description; // 상품 설명
    private String productImage; // 상품 이미지 URL
    private int price; // 상품 가격
    private int stock; // 재고 수량

    public Product(String productName, String description, String productImage, int price, int stock) {
        this.productName = productName;
        this.description = description;
        this.productImage = productImage;
        this.price = price;
        this.stock = stock;

    }
}
