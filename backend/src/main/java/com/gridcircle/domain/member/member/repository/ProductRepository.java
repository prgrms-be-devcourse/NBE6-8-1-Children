package com.gridcircle.domain.member.member.repository;

import com.gridcircle.domain.member.member.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
