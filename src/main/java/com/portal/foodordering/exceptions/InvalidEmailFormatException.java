package com.portal.foodordering.exceptions;

public class InvalidEmailFormatException extends RuntimeException{
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
