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
//    Optional<ProductVariantEntity> findById(Long id);

    // Lọc theo khoảng giá
    @Query("SELECT pv FROM ProductVariantEntity pv " +
            "WHERE pv.price BETWEEN :minPrice AND :maxPrice")
    List<ProductVariantEntity> findByPriceRange(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice);

    // Sắp xếp theo tiêu chí
    @Query("SELECT pv FROM ProductVariantEntity pv " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'newest' THEN pv.created END DESC, " +
            "CASE WHEN :sortBy = 'price_asc' THEN pv.price END ASC, " +
            "CASE WHEN :sortBy = 'price_desc' THEN pv.price END DESC, " +
            "CASE WHEN :sortBy = 'name_asc' THEN pv.name END ASC "
//            "CASE WHEN :sortBy = 'rating' THEN pv.rating END DESC"
           )
    List<ProductVariantEntity> findAllSortedBy(
            @Param("sortBy") String sortBy);
}

