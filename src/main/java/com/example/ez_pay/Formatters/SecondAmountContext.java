package com.example.ez_pay.Formatters;

import java.math.BigDecimal;

public class SecondAmountContext {
    private final BigDecimal firstAmount;
    private final BigDecimal secondAmount;

    public SecondAmountContext(BigDecimal firstAmount, BigDecimal secondAmount) {
        this.firstAmount = firstAmount;
        this.secondAmount = secondAmount;
    }

    public BigDecimal getFirstAmount() {
        return firstAmount;
    }

    public BigDecimal getSecondAmount() {
        return secondAmount;
    }
}

