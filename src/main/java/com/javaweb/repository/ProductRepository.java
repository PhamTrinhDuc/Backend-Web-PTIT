package com.javaweb.repository;

import com.javaweb.model.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {
    List<ProductsEntity> findAll();
}
