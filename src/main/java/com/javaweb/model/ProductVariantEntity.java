package com.javaweb.model;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.Map;

@Entity
@Table(name="product_variant")
public class ProductVariantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> specification;

    @Column(name = "discount")
    private double discount;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity_stock")
    private int quantity_stock;

    @Column(name = "created")
    private java.sql.Timestamp created;

    @Column(name = "updated")
    private java.sql.Timestamp updated;

    @ManyToOne()
    @JoinColumn(name = "product_id", nullable = false) // foreign key
    public ProductsEntity products;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Map<String, Object> getSpecification() {
        return specification;
    }

    public void setSpecification(Map<String, Object> specification) {
        this.specification = specification;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantityStock() {
        return quantity_stock;
    }

    public void setQuantityStock(int quantity_stock) {
        this.quantity_stock = quantity_stock;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public ProductsEntity getProducts() {
        return products;
    }

    public void setProducts(ProductsEntity products) {
        this.products = products;
    }
}