package com.example.ez_pay.Services;

import com.example.ez_pay.DTOs.Request.InvoicePaymentRequest;
import com.example.ez_pay.DTOs.Response.InvoicePaymentResponse;

public interface PaymentService {
    InvoicePaymentResponse processInvoicePayment(InvoicePaymentRequest invoicePaymentRequest);
}
