package com.example.ez_pay.Exceptions;

public class InvoiceCannotBePaidException extends RuntimeException {
    public InvoiceCannotBePaidException(String message) {
        super(message);
    }
}
