package com.portal.foodordering.exceptions;

public class ItemCantBeRemovedException extends RuntimeException{

    public ItemCantBeRemovedException(String message) {
        super(message);
    }
}
