package com.gridcircle.domain.product.product.dto;

import com.gridcircle.domain.product.product.entity.Product;

import java.time.LocalDateTime;

public record ProductDto (
        int id,
        String productName, // 상품명
        String description, // 상품 설명
        String productImage, // 상품 이미지 URL
        int price, // 상품 가격
        int stock, // 재고 수량
        LocalDateTime createDate,
        LocalDateTime midifiedDate
){
    public ProductDto(Product product) {
        this(
                product.getId(),
                product.getProductName(),
                product.getDescription(),
                product.getProductImage(),
                product.getPrice(),
                product.getStock(),
                product.getCreatedDate(),
                product.getModifiedDate()
        );
    }
}
