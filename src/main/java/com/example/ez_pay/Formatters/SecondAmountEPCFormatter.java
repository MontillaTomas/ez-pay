package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class SecondAmountEPCFormatter implements EPCFormatter<SecondAmountContext> {
    @Override
    public String format(SecondAmountContext ctx) {
        BigDecimal firstAmount = ctx.getFirstAmount();
        BigDecimal secondAmount = ctx.getSecondAmount();
        if (secondAmount == null || secondAmount.compareTo(BigDecimal.ZERO) == 0) {
            return "000000";
        }
        BigDecimal amount = firstAmount.subtract(secondAmount);
        String amountStr = amount.setScale(2, RoundingMode.DOWN).toPlainString();
        amountStr = amountStr.replaceAll("\\D", ""); // Remove non-digit characters

        if (amountStr.length() > 6) {
            amountStr = amountStr.substring(amountStr.length() - 6);
        }

        amountStr = String.format("%6s", amountStr).replace(' ', '0');
        return amountStr;
    }
}

