package com.javaweb.dto;

public class OrderDetailDTO {
    private Long variantId;
    private Integer quantity;
    private Double unit_price;
    private Double discount;

    public OrderDetailDTO() {
    }

    // Getters and Setters
    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unit_price;
    }

    public void setUnitPrice(Double unit_price) {
        this.unit_price = unit_price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
