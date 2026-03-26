package com.javaweb.repository;

import com.javaweb.model.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    @Query("SELECT COUNT(od) > 0 FROM OrderDetailEntity od " +
           "WHERE od.order.user.id = :userId " +
           "AND od.products.id = :productId " +
           "AND od.order.status = 'COMPLETED'")
    boolean hasPurchasedProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}