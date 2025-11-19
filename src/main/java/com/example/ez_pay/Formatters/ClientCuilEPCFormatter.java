package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

@Component
public class ClientCuilEPCFormatter implements EPCFormatter<String> {
    @Override
    public String format(String cuil) {
        if (cuil == null) cuil = "";
        String cleanedCuil = cuil.replaceAll("\\D", ""); // Remove non-digit characters
        return String.format("%14s", cleanedCuil).replace(' ', '0');
    }
}
