package com.javaweb.repository;

import com.javaweb.model.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {

    @Query("SELECT p FROM ProductsEntity p " +
            "WHERE (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<ProductsEntity> findAll(Double minPrice, Double maxPrice, Pageable pageable);

    @Query("SELECT p FROM ProductsEntity p WHERE p.category.slug = :categorySlug " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<ProductsEntity> findByCategorySlug(String categorySlug, Double minPrice, Double maxPrice, Pageable pageable);

    boolean existsByName(String name);
    List<ProductsEntity> findAllByOrderByCreatedAtDesc();
    Page<ProductsEntity> findByDiscountGreaterThan(Double discount, Pageable pageable);
    @Query("SELECT p FROM ProductsEntity p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<ProductsEntity> findByNameContainingIgnoreCase(
            @Param("keyword") String keyword,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);
}