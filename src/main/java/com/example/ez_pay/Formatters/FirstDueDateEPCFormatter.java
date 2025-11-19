package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FirstDueDateEPCFormatter implements EPCFormatter<LocalDate> {
    @Override
    public String format(LocalDate dueDate) {
        if (dueDate == null) return "00000"; // YY + DDD => at least 5 chars
        int year = dueDate.getYear() % 100;
        int dayOfYear = dueDate.getDayOfYear();
        return String.format("%02d%03d", year, dayOfYear);
    }
}
