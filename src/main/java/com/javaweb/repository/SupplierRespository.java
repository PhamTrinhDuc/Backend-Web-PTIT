package com.javaweb.repository;

import com.javaweb.model.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRespository extends JpaRepository<SupplierEntity, Long> {
    Optional<SupplierEntity> findByName(String supplierName);
}
