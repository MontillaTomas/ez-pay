package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

@Component
public class CurrencyEPCFormatter implements EPCFormatter<Object> {
    @Override
    public String format(Object input) {
        return "0"; // Pesos
    }
}

