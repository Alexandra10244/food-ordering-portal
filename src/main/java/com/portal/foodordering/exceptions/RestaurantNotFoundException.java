package com.portal.foodordering.exceptions;

public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
