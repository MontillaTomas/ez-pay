package com.example.ez_pay.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCalculationResult {
    private BigDecimal amountToApply;
    private LocalDate appliedDueDate;
    private boolean canBePaid;
}
