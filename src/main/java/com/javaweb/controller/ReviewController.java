package com.javaweb.controller;

import com.javaweb.dto.ReviewDTO;
import com.javaweb.dto.ReviewRequest;
import com.javaweb.model.ResponseObject;
import com.javaweb.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request, @RequestParam Long userId) {
        try {
            ReviewDTO reviewDTO = reviewService.createReview(request, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseObject.success(reviewDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseObject.error(e.getMessage(), HttpStatus.BAD_REQUEST));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseObject<Page<ReviewDTO>>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<ReviewDTO> reviews = reviewService.getReviewsByProductId(productId, pageable);
        return ResponseEntity.ok(ResponseObject.success(reviews));
    }

    @GetMapping("/product/{productId}/summary")
    public ResponseEntity<ResponseObject<Map<String, Object>>> getProductReviewSummary(@PathVariable Long productId) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("averageRating", reviewService.getAverageRating(productId));
        summary.put("totalReviews", reviewService.getReviewCount(productId));
        return ResponseEntity.ok(ResponseObject.success(summary));
    }

    @GetMapping("/check")
    public ResponseEntity<ResponseObject<Map<String, Boolean>>> checkReviewStatus(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        Map<String, Boolean> status = new HashMap<>();
        status.put("canReview", reviewService.hasUserPurchasedProduct(userId, productId) 
                && !reviewService.hasUserReviewedProduct(userId, productId));
        status.put("alreadyReviewed", reviewService.hasUserReviewedProduct(userId, productId));
        return ResponseEntity.ok(ResponseObject.success(status));
    }
}
