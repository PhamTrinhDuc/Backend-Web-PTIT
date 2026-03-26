package com.javaweb.dto;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {
    @NotBlank(message = "Google Token cannot be blank")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
