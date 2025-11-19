package com.example.ez_pay.Formatters;

import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
public class SecondDueDateEPCFormatter implements EPCFormatter<SecondDueDateContext> {
    @Override
    public String format(SecondDueDateContext ctx) {
        if (ctx.getSecondDueDate() == null) {
            return "00";
        }
        int daysBetween = (int) ChronoUnit.DAYS.between(ctx.getFirstDueDate(), ctx.getSecondDueDate());
        return String.format("%02d", daysBetween);
    }
}

