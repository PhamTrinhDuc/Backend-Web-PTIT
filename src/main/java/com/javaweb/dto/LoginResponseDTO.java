package com.javaweb.dto;

public class LoginResponseDTO {
    private String token;
    private UserDTO user;

    public LoginResponseDTO(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UserDTO getUser() { return user; }
}
