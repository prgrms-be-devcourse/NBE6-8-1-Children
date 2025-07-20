package com.gridcircle.domain.shoppingbasket.shoppingbasket.repository;

import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingBasketRepository extends JpaRepository<ShoppingBasket, Integer>{

    List<ShoppingBasket> findByMemberId(int memberId);

    // 장바구니 비우기위해 메서드 추가
    void deleteByMemberId(int memberId);
}

