package com.example.ez_pay.DTOs.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceUpdateRequest {
    private String receiverName;
    private String receiverCUIL;
    private BigDecimal amount;
    private LocalDate creationDate;
    private LocalDate expirationDate;
}
