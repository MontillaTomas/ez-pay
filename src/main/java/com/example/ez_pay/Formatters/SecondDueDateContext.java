package com.example.ez_pay.Formatters;

import java.time.LocalDate;

public class SecondDueDateContext {
    private final LocalDate firstDueDate;
    private final LocalDate secondDueDate;

    public SecondDueDateContext(LocalDate firstDueDate, LocalDate secondDueDate) {
        this.firstDueDate = firstDueDate;
        this.secondDueDate = secondDueDate;
    }

    public LocalDate getFirstDueDate() {
        return firstDueDate;
    }

    public LocalDate getSecondDueDate() {
        return secondDueDate;
    }
}

