package com.jwttemplate.api.auth.controller.exception;

public class UserAlreadyRegisteredException extends RuntimeException {
    public UserAlreadyRegisteredException() {
        super("Subscription not found");
    }
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
