package com.javaweb.dto;

import com.javaweb.model.FlashSaleEntity;
import com.javaweb.model.ProductsEntity;
import com.javaweb.model.ProductImageEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlashSaleDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private List<FlashSaleProductDTO> products;

    public FlashSaleDTO(FlashSaleEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.description = entity.getDescription();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.status = entity.getStatus();
        if (entity.getProducts() != null) {
            this.products = entity.getProducts().stream()
                    .map(FlashSaleProductDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<FlashSaleProductDTO> getProducts() { return products; }
    public void setProducts(List<FlashSaleProductDTO> products) { this.products = products; }

    public static class FlashSaleProductDTO {
        private Long id;
        private String name;
        private double price;
        private double discount;
        private List<String> imagePaths;

        public FlashSaleProductDTO(ProductsEntity product) {
            this.id = product.getId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.discount = product.getDiscount();
            if (product.getProductImageEntities() != null) {
                this.imagePaths = product.getProductImageEntities().stream()
                        .map(ProductImageEntity::getImagePath)
                        .collect(Collectors.toList());
            }
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public double getDiscount() { return discount; }
        public void setDiscount(double discount) { this.discount = discount; }
        public List<String> getImagePaths() { return imagePaths; }
        public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
    }
}
