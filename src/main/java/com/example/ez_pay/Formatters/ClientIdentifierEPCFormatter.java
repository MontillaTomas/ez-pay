package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

@Component
public class ClientIdentifierEPCFormatter implements EPCFormatter<String> {
    @Override
    public String format(String clienIdentIdentifier) {
        if (clienIdentIdentifier == null) {
            return "00000000000000";
        }
        return clienIdentIdentifier;
    }
}
