package com.example.ez_pay.Mappers;

import com.example.ez_pay.DTOs.Response.InvoicePaymentResponse;
import com.example.ez_pay.Models.PaymentReceipt;
import org.springframework.stereotype.Component;

@Component
public class PaymentReceiptMapper {
    public InvoicePaymentResponse toResponse(PaymentReceipt entity) {
        if (entity == null) {
            return null;
        }

        InvoicePaymentResponse dto = new InvoicePaymentResponse();
        dto.setPaymentReceiptId(entity.getId());
        dto.setPaymentDateTime(entity.getPaymentDateTime());
        dto.setAmountPaid(entity.getAmountPaid());
        dto.setReceiverName(entity.getInvoice().getReceiverName());
        dto.setReceiverCUIL(entity.getInvoice().getReceiverCUIL());
        dto.setEpc(entity.getInvoice().getPaymentStub().getElectronicPaymentCode());
        dto.setEmployeeId(entity.getEmployee().getId());
        dto.setPaymentPointId(entity.getPaymentPoint().getId());

        return dto;
    }
}
