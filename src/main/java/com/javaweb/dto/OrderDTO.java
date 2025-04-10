package com.javaweb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


public class OrderDTO {
    private Long userId;
    private List<OrderDetailDTO> items;
    private String paymentMethod;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OrderDetailDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderDetailDTO> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
