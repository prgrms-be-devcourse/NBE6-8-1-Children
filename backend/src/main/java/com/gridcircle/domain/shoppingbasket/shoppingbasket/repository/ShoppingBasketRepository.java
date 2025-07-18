package com.gridcircle.domain.shoppingbasket.shoppingbasket.repository;

import com.gridcircle.domain.product.product.entity.Product;
import com.gridcircle.domain.product.product.repository.ProductRepository;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, Integer>{

    List<ShoppingBasket> findByMemberId(int memberId);
}

