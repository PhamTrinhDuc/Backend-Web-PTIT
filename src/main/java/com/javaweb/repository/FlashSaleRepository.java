package com.javaweb.repository;

import com.javaweb.model.FlashSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface FlashSaleRepository extends JpaRepository<FlashSaleEntity, Long> {
    @Query("SELECT f FROM FlashSaleEntity f WHERE f.status = 'active' ORDER BY f.endDate DESC LIMIT 1")
    Optional<FlashSaleEntity> findLatestActiveFlashSale();
}
