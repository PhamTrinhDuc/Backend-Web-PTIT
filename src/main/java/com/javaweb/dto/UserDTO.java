package com.javaweb.dto;

import com.javaweb.model.UserEntity;
import com.javaweb.model.UserProfileEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String fullname;
    private String phone_number;
    private String avatar;
    private String birthday;
    private String gender;
    private String address;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDTO(UserEntity user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole();
        UserProfileEntity userProfile = user.getUserProfile();
        if (userProfile != null) {
            this.fullname = userProfile.getFullname();
            this.phone_number = userProfile.getPhone();
            this.avatar = userProfile.getAvatar();
            this.birthday = userProfile.getBirthday();
            this.gender = userProfile.getGender();
            this.address = userProfile.getAddress();
            this.status = userProfile.getStatus();
            this.createdAt = userProfile.getCreatedAt();
            this.updatedAt = userProfile.getUpdatedAt();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}