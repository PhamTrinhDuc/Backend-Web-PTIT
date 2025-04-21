package com.javaweb.repository;

import com.javaweb.model.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {
    Page<ProductsEntity> findByCategorySlug(String categorySlug, Pageable pageable);
    boolean existsByName(String name);
    List<ProductsEntity> findAllByOrderByCreatedAtDesc();
    List<ProductsEntity> findByDiscountGreaterThan(Double discount);
    List<ProductsEntity> findByPriceBetween(Double minPrice, Double maxPrice);
    List<ProductsEntity> findAllByOrderByPriceAsc();
    List<ProductsEntity> findAllByOrderByPriceDesc();
    List<ProductsEntity> findByPriceGreaterThanEqual(Double minPrice);
    List<ProductsEntity> findByPriceLessThanEqual(Double maxPrice);
    List<ProductsEntity> findAllByOrderByNameAsc();
    List<ProductsEntity> findAllByOrderByNameDesc();
    List<ProductsEntity> findByNameContainingIgnoreCase(String keyword);
}