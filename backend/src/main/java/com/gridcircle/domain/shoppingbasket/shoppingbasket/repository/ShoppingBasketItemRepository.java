package com.gridcircle.domain.shoppingbasket.shoppingbasket.repository;

import com.gridcircle.domain.member.member.entity.Member;
import com.gridcircle.domain.shoppingbasket.shoppingbasket.entity.ShoppingBasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingBasketItemRepository extends JpaRepository<ShoppingBasketItem, Integer>{

    //Optional<ShoppingBasketItem> findByMemberId(int memberId);
    List<ShoppingBasketItem> findAllByMember(Member member);
}

