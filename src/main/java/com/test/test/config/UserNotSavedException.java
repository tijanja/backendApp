package com.test.test.config;

public class UserNotSavedException extends RuntimeException {
    public UserNotSavedException(String error_user_not_saved) {
        super(error_user_not_saved);
    }
}
