package com.example.ez_pay.Services.impl;

import com.example.ez_pay.Builders.ElectronicPaymentCodeBuilder;
import com.example.ez_pay.Exceptions.PaymentOverdueException;
import com.example.ez_pay.Formatters.*;
import com.example.ez_pay.Models.BarcodeType;
import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Models.PaymentStub;
import com.example.ez_pay.Services.PaymentStubService;
import com.example.ez_pay.ValueObject.PaymentCalculationResult;
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

    @Override
    public PaymentCalculationResult calculatePaymentStubData(Invoice invoice) {
        LocalDate today = LocalDate.now();
        // If there is no due date, apply the original amount
        if (invoice.getDueDate() == null) {
            return PaymentCalculationResult.builder()
                    .amountToApply(invoice.getAmount())
                    .appliedDueDate(null)
                    .build();
        }
        // If there is no second due date and today is after the due date, raise an exception
        if (invoice.getSecondDueDate() == null && today.isAfter(invoice.getDueDate())) {
            throw new PaymentOverdueException("Payment is overdue and can not be paid.");
        }
        // If today is on or before the due date, apply the original amount
        if (today.isBefore(invoice.getDueDate()) || today.isEqual(invoice.getDueDate())) {
            return PaymentCalculationResult.builder()
                    .amountToApply(invoice.getAmount())
                    .appliedDueDate(invoice.getDueDate())
                    .build();
        }
        // If today is after the due date but on or before the second due date, apply the second amount
        if (invoice.getSecondDueDate() != null &&
                (today.isBefore(invoice.getSecondDueDate()) || today.isEqual(invoice.getSecondDueDate()))) {
            return PaymentCalculationResult.builder()
                    .amountToApply(invoice.getSecondAmount())
                    .appliedDueDate(invoice.getSecondDueDate())
                    .build();
        }
        // If today is after the second due date, raise an exception
        throw new PaymentOverdueException("Payment is overdue and no valid due date is available.");
    }
}
