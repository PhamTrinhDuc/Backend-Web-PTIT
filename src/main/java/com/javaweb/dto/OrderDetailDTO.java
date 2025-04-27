package com.javaweb.dto;

public class OrderDetailDTO {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double unitPrice; // Đổi từ price thành unitPrice
    private double discount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() {return productName;}
    public void setProductName(String productName) {this.productName = productName;}
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
}