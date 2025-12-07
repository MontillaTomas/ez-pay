package com.example.ez_pay.Enricher;

import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Services.PaymentStubService;
import com.example.ez_pay.ValueObject.PaymentCalculationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStubEnricher {

    private final PaymentStubService paymentStubService;

    public PaymentCalculationResult enrich(Invoice invoice) {
        return paymentStubService.calculatePaymentStubData(invoice);
    }
}