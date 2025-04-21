package com.javaweb.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// backend: MVC -> function + DB; show API: input + output

@Entity
@Table(name = "supplier")
public class SupplierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_name")
    private String name;

    @Column(name = "contact_info")
    private String contactInfo;

    @Column(name = "is_active")
    private boolean isActive;

    // Quan hệ nhiều-nhiều với Product
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductsEntity> products;

    // Constructors
    public SupplierEntity() {}

    public SupplierEntity(String name, String contactInfo, String categoryName, boolean isActive) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.isActive = isActive;
    }

    // Getters và Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplierName() {
        return name;
    }

    public void setSupplierName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ProductsEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsEntity> products) {
        this.products = products;
    }
}