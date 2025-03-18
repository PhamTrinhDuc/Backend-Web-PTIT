package com.javaweb.repository.entity;

public class ProductEntity {
    private String product_name;
    private Float price;
    private String quantity_stock;
    private String description;
    private Integer category_id;

    public String getProductName() {
        return product_name;
    }

    public Float getPrice() {
        return price;
    }

    public String getQuantityStock() {
        return quantity_stock;
    }

    public Integer getSupplierId() {
        return category_id;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public void setProductName(String product_name) {
        this.product_name = product_name;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setQuantityStock(String quantity_stock) {
        this.quantity_stock = quantity_stock;
    }

    public void setSupplierId(Integer category_id) {
        this.category_id = category_id;
    }
}
