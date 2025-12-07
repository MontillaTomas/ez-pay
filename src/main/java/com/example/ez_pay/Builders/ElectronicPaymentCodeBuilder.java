package com.example.ez_pay.Builders;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ElectronicPaymentCodeBuilder {

    public String build(List<String> parts) {
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            sb.append(p);
        }

        String dataWithoutCheckDigit = sb.toString();
        String firstCheckDigit = calculateCheckDigit(dataWithoutCheckDigit);
        sb.append(firstCheckDigit);
        String dataWithFirstCheckDigit = sb.toString();
        String secondCheckDigit = calculateCheckDigit(dataWithFirstCheckDigit);
        sb.append(secondCheckDigit);

        return sb.toString();
    }

    private String calculateCheckDigit(String data) {
        int[] weights = {3,5,7,9};
        int sum = 0;
        int weightIndex = 0;

        if (data == null || data.isEmpty()) return "0";

        // First iteration using weight = 1 for first digit
        int firstDigit = data.charAt(0) - '0';
        sum += firstDigit;

        for (int i = 1; i < data.length(); i++) {
            int digit = data.charAt(i) - '0';
            sum += digit * weights[weightIndex];
            weightIndex = (weightIndex + 1) % weights.length;
        }

        int half = sum / 2;
        return String.valueOf(half % 10);
    }
}
