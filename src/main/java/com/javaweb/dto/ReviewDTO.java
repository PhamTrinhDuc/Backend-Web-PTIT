package com.javaweb.dto;

import com.javaweb.model.ReviewEntity;
import com.javaweb.model.ReviewImageEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDTO {
    private Long id;
    private Long userId;
    private String username;
    private String fullname;
    private String userAvatar;
    private Long productId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private List<String> imageUrls;

    public ReviewDTO() {}

    public ReviewDTO(ReviewEntity review) {
        this.id = review.getId();
        this.userId = review.getUser().getId();
        this.username = review.getUser().getUsername();
        this.fullname = review.getUser().getFullname();
        this.userAvatar = review.getUser().getAvatar();
        this.productId = review.getProduct().getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.createdAt = review.getCreatedAt();
        this.imageUrls = review.getImages().stream()
                .map(ReviewImageEntity::getImageUrl)
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getUserAvatar() { return userAvatar; }
    public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
}
