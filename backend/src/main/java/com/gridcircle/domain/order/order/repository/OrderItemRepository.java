package com.gridcircle.domain.order.order.repository;

import com.gridcircle.domain.order.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
