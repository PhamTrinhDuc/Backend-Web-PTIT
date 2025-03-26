package com.javaweb.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariantEntity productVariant;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "add_at", nullable = false)
    private LocalDateTime addAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CartEntity getCart() { return cart; }
    public void setCart(CartEntity cart) { this.cart = cart; }
    public ProductVariantEntity getVariant() { return productVariant; }
    public void setVariant(ProductVariantEntity variant) { this.productVariant = variant; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getAddAt() { return addAt; }
    public void setAddAt(LocalDateTime addAt) { this.addAt = addAt; }
}