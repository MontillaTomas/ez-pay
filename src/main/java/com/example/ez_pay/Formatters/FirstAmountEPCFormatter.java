package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class FirstAmountEPCFormatter implements EPCFormatter<BigDecimal> {
    @Override
    public String format(BigDecimal amount) {
        if (amount == null) amount = BigDecimal.ZERO;
        String amountStr = amount.setScale(2, RoundingMode.DOWN).toPlainString();
        amountStr = amountStr.replaceAll("\\D", ""); // Remove non-digit characters

        if (amountStr.length() > 8) {
            amountStr = amountStr.substring(amountStr.length() - 8);
        }

        amountStr = String.format("%8s", amountStr).replace(' ', '0');
        return amountStr;
    }
}
