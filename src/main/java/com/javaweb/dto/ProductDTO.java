package com.javaweb.dto;

import com.javaweb.model.ProductImageEntity;
import com.javaweb.model.ProductsEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductDTO {

    private Long id;
    private Long categoryId;
    private String name;
    private Double price;
    private Integer quantityStock;
    private Map<String, Object> specification;
    private Double discount;
    private String description;
    private List<String> imagePaths;

    public ProductDTO() {
    }

    public ProductDTO(ProductsEntity productsEntity) {
        this.id = productsEntity.getId();
        this.name = productsEntity.getName();
        this.price = productsEntity.getPrice();
        this.quantityStock = productsEntity.getQuantityStock();
        this.specification = productsEntity.getSpecification();
        this.discount = productsEntity.getDiscount();
        this.description = productsEntity.getDescription();
        this.imagePaths = productsEntity.getProductImageEntities()
                .stream()
                .map(ProductImageEntity::getImagePath)
                .collect(Collectors.toList());
        this.categoryId = productsEntity.getCategory() != null ? productsEntity.getCategory().getId() : null; // Thêm categoryId
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
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

    public Integer getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(Integer quantityStock) {
        this.quantityStock = quantityStock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
