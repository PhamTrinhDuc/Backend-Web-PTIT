package com.javaweb.dto;

import com.javaweb.model.ProductImageEntity;
import com.javaweb.model.ProductsEntity;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Map<String, Object> specification;
    private Double discount;
    private String description;
    private Integer quantityStock;
    private Long categoryId;
    private Long supplierId;
    private List<String> imagePaths;
    private Integer soldQuantity; // Thêm trường này

    // Constructors
    public ProductDTO() {}

    public ProductDTO(ProductsEntity product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.specification = product.getSpecification();
        this.discount = product.getDiscount();
        this.description = product.getDescription();
        this.quantityStock = product.getQuantityStock();
        this.categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        this.supplierId = product.getSupplier() != null ? product.getSupplier().getId() : null;
        this.imagePaths = product.getProductImageEntities().stream()
                .map(ProductImageEntity::getImagePath)
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Map<String, Object> getSpecification() { return specification; }
    public void setSpecification(Map<String, Object> specification) { this.specification = specification; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getQuantityStock() { return quantityStock; }
    public void setQuantityStock(Integer quantityStock) { this.quantityStock = quantityStock; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }
    public Integer getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(Integer soldQuantity) { this.soldQuantity = soldQuantity; }
}