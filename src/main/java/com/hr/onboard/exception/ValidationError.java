package com.hr.onboard.exception;

public class ValidationError extends IllegalArgumentException {
    public ValidationError(String message){
        super(message);
    }
}
