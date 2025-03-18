package com.javaweb.custom_exception;

public class FieldRequiredException  extends RuntimeException{
    public FieldRequiredException(String message) {
        super(message);
    }
}
