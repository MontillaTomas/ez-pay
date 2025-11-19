package com.example.ez_pay.Services.impl;

import com.example.ez_pay.Builders.ElectronicPaymentCodeBuilder;
import com.example.ez_pay.Formatters.*;
import com.example.ez_pay.Models.BarcodeType;
import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Models.PaymentStub;
import com.example.ez_pay.Services.PaymentStubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentStubServiceImpl implements PaymentStubService {

    private final ServiceNumberEPCFormatter serviceNumberFormatter;
    private final FirstAmountEPCFormatter firstAmountFormatter;
    private final FirstDueDateEPCFormatter firstDueDateFormatter;
    private final ClientCuilEPCFormatter clientCuilFormatter;
    private final CurrencyEPCFormatter currencyFormatter;
    private final SecondAmountEPCFormatter secondAmountFormatter;
    private final SecondDueDateEPCFormatter secondDueDateFormatter;
    private final ElectronicPaymentCodeBuilder codeBuilder;

    @Autowired
    public PaymentStubServiceImpl(ServiceNumberEPCFormatter serviceNumberFormatter,
                                  FirstAmountEPCFormatter firstAmountFormatter,
                                  FirstDueDateEPCFormatter firstDueDateFormatter,
                                  ClientCuilEPCFormatter clientCuilFormatter,
                                  CurrencyEPCFormatter currencyFormatter,
                                  SecondAmountEPCFormatter secondAmountFormatter,
                                  SecondDueDateEPCFormatter secondDueDateFormatter,
                                  ElectronicPaymentCodeBuilder codeBuilder) {
        this.serviceNumberFormatter = serviceNumberFormatter;
        this.firstAmountFormatter = firstAmountFormatter;
        this.firstDueDateFormatter = firstDueDateFormatter;
        this.clientCuilFormatter = clientCuilFormatter;
        this.currencyFormatter = currencyFormatter;
        this.secondAmountFormatter = secondAmountFormatter;
        this.secondDueDateFormatter = secondDueDateFormatter;
        this.codeBuilder = codeBuilder;
    }

    @Override
    public PaymentStub generatePaymentStub(Invoice invoice) {
        List<String> parts = new ArrayList<>();
        parts.add(serviceNumberFormatter.format(invoice.getCompany()));
        parts.add(firstAmountFormatter.format(invoice.getAmount()));
        parts.add(firstDueDateFormatter.format(invoice.getDueDate()));
        parts.add(clientCuilFormatter.format(invoice.getReceiverCUIL()));
        parts.add(currencyFormatter.format(null));
        parts.add(secondAmountFormatter.format(new SecondAmountContext(invoice.getAmount(), invoice.getSecondAmount())));
        parts.add(secondDueDateFormatter.format(new SecondDueDateContext(invoice.getDueDate(), invoice.getSecondDueDate())));

        String code = codeBuilder.build(parts);

        PaymentStub paymentStub = new PaymentStub();
        paymentStub.setElectronicPaymentCode(code);
        paymentStub.setBarcodeType(BarcodeType.CODE_128C);
        paymentStub.setCreatedAt(LocalDate.now());
        return paymentStub;
    }
}
