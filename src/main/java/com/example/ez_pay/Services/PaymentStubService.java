package com.example.ez_pay.Services;

import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Models.PaymentStub;
import com.example.ez_pay.ValueObject.PaymentCalculationResult;

public interface PaymentStubService {
    PaymentStub generatePaymentStub(Invoice invoice);
    PaymentCalculationResult calculatePaymentStubData(Invoice invoice);
}
