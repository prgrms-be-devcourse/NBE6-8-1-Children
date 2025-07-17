package com.gridcircle.domain.order.order.repository;

import com.gridcircle.domain.order.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByMemberId(int memberId);
}
