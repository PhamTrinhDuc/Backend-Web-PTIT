package com.javaweb.dto;

public class RegisterResponseDTO {
    private UserDTO user;
    private String message;

    public RegisterResponseDTO(UserDTO user, String message) {
        this.user = user;
        this.message = message;
    }

    public UserDTO getUser() { return user; }
    public String getMessage() { return message; }
}
