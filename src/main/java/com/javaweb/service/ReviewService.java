package com.javaweb.service;

import com.javaweb.dto.ReviewDTO;
import com.javaweb.dto.ReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(ReviewRequest reviewRequest, Long userId);
    Page<ReviewDTO> getReviewsByProductId(Long productId, Pageable pageable);
    Double getAverageRating(Long productId);
    Long getReviewCount(Long productId);
    boolean hasUserPurchasedProduct(Long userId, Long productId);
    boolean hasUserReviewedProduct(Long userId, Long productId);
}
