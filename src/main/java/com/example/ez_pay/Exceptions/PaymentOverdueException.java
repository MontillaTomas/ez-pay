package com.example.ez_pay.Exceptions;

public class PaymentOverdueException extends RuntimeException {
    public PaymentOverdueException(String message) {
        super(message);
    }
}
