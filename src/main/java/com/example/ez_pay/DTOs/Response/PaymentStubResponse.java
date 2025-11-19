package com.example.ez_pay.DTOs.Response;

import com.example.ez_pay.Models.BarcodeType;
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
public class PaymentStubResponse {
    private UUID id;
    private String electronicPaymentCode;
    private BarcodeType barcodeType;
    private BigDecimal currentAmount;
    private LocalDate appliedDueDate;
    private boolean canBePaid;
}
