package com.javaweb.repository;

import com.javaweb.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByUser_Id(Long userId);
}
