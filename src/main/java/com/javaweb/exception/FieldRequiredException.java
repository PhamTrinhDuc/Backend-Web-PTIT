package com.javaweb.exception;

public class FieldRequiredException  extends RuntimeException{
    public FieldRequiredException(String message) {
        super(message);
    }
}
