package com.portal.foodordering.exceptions;

public class PaymentStatusException extends RuntimeException{
    public PaymentStatusException(String message) {
        super(message);
    }
}
