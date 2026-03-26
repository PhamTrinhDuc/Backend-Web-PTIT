package com.javaweb.repository;

import com.javaweb.model.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Page<ReviewEntity> findByProductId(Long productId, Pageable pageable);
    List<ReviewEntity> findByProductId(Long productId);
    List<ReviewEntity> findByUserId(Long userId);
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}
