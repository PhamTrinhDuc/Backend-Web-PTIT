package com.javaweb.dto;

import com.javaweb.model.UserEntity;

public class UserSpendingDTO {
    private UserEntity user;
    private double totalSpending;

    public UserSpendingDTO(UserEntity user, double totalSpending) {
        this.user = user;
        this.totalSpending = totalSpending;
    }

    public UserEntity getUser() {
        return user;
    }

    public double getTotalSpending() {
        return totalSpending;
    }
}