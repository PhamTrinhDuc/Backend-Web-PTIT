package com.javaweb.model;

public class ProductDTO {
    private String productName;
    private Float price;
    private String quantityStock;
    private String description;

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public String getQuantityStock() {
        return quantityStock;
    }
    public void setQuantityStock(String quantityStock) {
        this.quantityStock = quantityStock;
    }
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

}
