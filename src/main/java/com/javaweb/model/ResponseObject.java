package com.javaweb.model;

import org.springframework.http.HttpStatus;

public class ResponseObject<T> {
    private boolean success;
    private String message;
    private T data;
    private HttpStatus status;

    public ResponseObject(boolean success, String message, T data, HttpStatus status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.status = status;
    }

    public static <T> ResponseObject<T> success(T data) {
        return new ResponseObject<>(true, "Success", data, HttpStatus.OK);
    }

    public static <T> ResponseObject<T> error(String message, HttpStatus status) {
        return new ResponseObject<>(false, message, null, status);
    }

    // Getters & Setters
    public boolean Success() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
