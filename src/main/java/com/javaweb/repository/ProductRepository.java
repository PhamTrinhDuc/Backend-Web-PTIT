package com.javaweb.repository;

import com.javaweb.model.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductsEntity, Long> {
    boolean existsByName(String name);
    @Query("SELECT p FROM ProductsEntity p WHERE p.category.slug = :categorySlug")
    List<ProductsEntity> findByCategorySlug(@Param("categorySlug") String categorySlug);
}
