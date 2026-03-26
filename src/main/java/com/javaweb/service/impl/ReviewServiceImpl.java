package com.javaweb.service.impl;

import com.javaweb.dto.ReviewDTO;
import com.javaweb.dto.ReviewRequest;
import com.javaweb.model.ReviewEntity;
import com.javaweb.model.ReviewImageEntity;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.UserEntity;
import com.javaweb.repository.ProductRepository;
import com.javaweb.repository.ReviewRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.repository.OrderDetailRepository;
import com.javaweb.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public ReviewDTO createReview(ReviewRequest reviewRequest, Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ProductsEntity product = productRepository.findById(reviewRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!orderDetailRepository.hasPurchasedProduct(userId, product.getId())) {
             throw new RuntimeException("User has not purchased this product or order is not completed.");
        }

        if (reviewRepository.existsByUserIdAndProductId(userId, product.getId())) {
            throw new RuntimeException("User has already reviewed this product.");
        }

        ReviewEntity review = new ReviewEntity();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());

        List<ReviewImageEntity> images = new ArrayList<>();
        if (reviewRequest.getImageUrls() != null) {
            for (String url : reviewRequest.getImageUrls()) {
                ReviewImageEntity image = new ReviewImageEntity();
                image.setImageUrl(url);
                image.setReview(review);
                images.add(image);
            }
        }
        review.setImages(images);

        ReviewEntity savedReview = reviewRepository.save(review);
        return new ReviewDTO(savedReview);
    }

    @Override
    public Page<ReviewDTO> getReviewsByProductId(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable)
                .map(ReviewDTO::new);
    }

    @Override
    public Double getAverageRating(Long productId) {
        List<ReviewEntity> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .mapToInt(ReviewEntity::getRating)
                .average()
                .orElse(0.0);
    }

    @Override
    public Long getReviewCount(Long productId) {
        return (long) reviewRepository.findByProductId(productId).size();
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        return orderDetailRepository.hasPurchasedProduct(userId, productId);
    }

    @Override
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.existsByUserIdAndProductId(userId, productId);
    }
}
