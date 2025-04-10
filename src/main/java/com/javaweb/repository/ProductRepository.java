package com.javaweb.repository;

import com.javaweb.model.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {
    List<ProductsEntity> findByCategorySlug(String categorySlug);
    boolean existsByName(String name);

    // Thêm các phương thức mới
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