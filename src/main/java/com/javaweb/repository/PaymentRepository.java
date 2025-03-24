package com.javaweb.repository;

import com.javaweb.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByOrderId(Long orderId);
}
