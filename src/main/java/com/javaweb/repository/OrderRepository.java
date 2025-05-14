package com.javaweb.repository;

import com.javaweb.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByUser_Id(Pageable pageable, Long userId);
    Page<OrderEntity> findByUserId(Pageable pageable, Long userId);
}
