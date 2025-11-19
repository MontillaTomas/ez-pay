package com.example.ez_pay.Services;

import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Models.PaymentStub;

public interface PaymentStubService {
    PaymentStub generatePaymentStub(Invoice invoice);
}
