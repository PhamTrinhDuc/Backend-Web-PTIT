package com.javaweb.dto;

import com.javaweb.model.UserEntity;

import java.time.LocalDateTime;

public class UserDTO {

    private Integer id;
    private String username;
    private String role;
    private String email;
    private String fullname;
    private String avatar;
    private String gender;
    private String address;
    private String birthday;
    private String status;
    private String phoneNumber;
    private LocalDateTime createdAt;

    // Default constructor
    public UserDTO() {}

    // Constructor to map from UserEntity
    public UserDTO(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.email = user.getEmail();
        this.fullname = user.getFullname();
        this.avatar = user.getAvatar();
        this.gender = user.getGender();
        this.address = user.getAddress();
        this.birthday = user.getBirthday();
        this.status = user.getStatus();
        this.phoneNumber = user.getPhoneNumber();
        this.createdAt = user.getCreatedAt();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}