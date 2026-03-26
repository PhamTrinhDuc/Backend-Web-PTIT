package com.javaweb.dto;

public class TopSpendingUserDTO {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private double totalSpending;

    public TopSpendingUserDTO(Long id, String username, String fullname, String email, double totalSpending) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.totalSpending = totalSpending;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public double getTotalSpending() {
        return totalSpending;
    }
}