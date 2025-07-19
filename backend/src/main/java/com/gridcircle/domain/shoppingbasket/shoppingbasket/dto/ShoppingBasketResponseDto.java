//package com.gridcircle.domain.shoppingbasket.shoppingbasket.dto;
//
//import com.gridcircle.domain.product.product.entity.Product;
//import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasket;
//
//import java.time.LocalDateTime;
//
//public record ShoppingBasketResponseDto(
//        int productCount,
//        int Member member,
//        int product,
//        LocalDateTime createdDate,
//        LocalDateTime modifiedDate
//){
//    public ShoppingBasketDto(ShoppingBasket shoppingBasket) {
//        this(
//                shoppingBasket.getProductCount(),
//                // shoppingBasket.getMember(),
//                shoppingBasket.getProduct(),
//                shoppingBasket.getCreatedDate(),
//                shoppingBasket.getModifiedDate()
//        );
//    }
//}
