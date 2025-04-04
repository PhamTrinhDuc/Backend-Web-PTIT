

package com.javaweb.dto;

import jakarta.validation.constraints.Size;

public class UpdateUserRequestDTO {
    @Size(min = 1, max = 100, message = "Fullname must be between 1 and 100 characters")
    private String fullname;

    private String avatar;

    private String birthday;

    private String gender;

    private String address;

    // Getters and setters
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}