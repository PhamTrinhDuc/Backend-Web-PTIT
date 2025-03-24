package com.javaweb.dto;

import com.javaweb.model.ProductImageEntity;
import com.javaweb.model.ProductVariantEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductVariantDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer quantityStock;
    private Map<String, Object> specification;
    private Double discount;
    private String description;
    private List<String> imagePaths;

    public ProductVariantDTO(ProductVariantEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.quantityStock = entity.getQuantityStock();
        this.specification = entity.getSpecification();
        this.discount = entity.getDiscount();
        this.description = entity.getDescription();
        this.quantityStock = entity.getQuantityStock();
        this.imagePaths = entity.getProductImageEntityList()
                .stream()
                .map(ProductImageEntity::getImagePath)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Map<String, Object> getSpecification() {
        return specification;
    }

    public void setSpecification(Map<String, Object> specification) {
        this.specification = specification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantityStock() { return quantityStock; }

    public void setQuantityStock(Integer quantityStock) { this.quantityStock = quantityStock; }

    public List<String> getImagePaths() { return imagePaths; }

    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
}
