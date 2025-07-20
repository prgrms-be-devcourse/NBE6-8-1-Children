package com.gridcircle.domain.order.order.repository;

import com.gridcircle.domain.order.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByMemberId(int memberId);
}
