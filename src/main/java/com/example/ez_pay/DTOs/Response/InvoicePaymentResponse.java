package com.example.ez_pay.DTOs.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePaymentResponse {
    private UUID paymentReceiptId;
    private String epc;
    private BigDecimal amountPaid;
    private LocalDateTime paymentDateTime;
    private String receiverName;
    private String receiverCUIL;
    private Long employeeId;
    private Long paymentPointId;
}
