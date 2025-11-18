package com.example.ez_pay.DTOs.Response;

import com.example.ez_pay.Models.InvoiceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private UUID id;
    private Long companyId;
    private String receiverName;
    private String receiverCUIL;
    private BigDecimal amount;
    private BigDecimal secondAmount;
    private LocalDate dueDate;
    private LocalDate secondDueDate;
    private LocalDate issueDate;
    private InvoiceStatus status;
    private PaymentStubResponse paymentStub;
}
