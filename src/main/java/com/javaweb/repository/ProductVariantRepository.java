package com.javaweb.repository;

import com.javaweb.model.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
    boolean existsByName(String name);
    @Query("SELECT pv FROM ProductVariantEntity pv " +
            "WHERE pv.products.category.slug = :categorySlug")
    List<ProductVariantEntity> findByCategorySlug(@Param("categorySlug") String categorySlug);

    @Query("SELECT pv FROM ProductVariantEntity pv " +
            "ORDER BY pv.discount DESC")
    List<ProductVariantEntity> findAllSortedByDiscountDesc();
    Optional<ProductVariantEntity> findById(Long id);
}

