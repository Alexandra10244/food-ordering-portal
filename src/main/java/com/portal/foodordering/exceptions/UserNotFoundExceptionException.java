package com.portal.foodordering.exceptions;

public class UserNotFoundExceptionException extends RuntimeException{
    public UserNotFoundExceptionException(String message) {
        super(message);
    }
}

