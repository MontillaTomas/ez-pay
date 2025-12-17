package com.example.ez_pay.Models;

import com.example.ez_pay.Exceptions.ResourceNotFoundException;

public enum Role {
    EMPLEADO("Empleado"),
    EMPRESA("Empresa"),
    ADMIN("Admin");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Role fromText(String text) {

        String cleanText = text.trim();

        for (Role rol : Role.values()) {
            if (rol.getDisplayName().equalsIgnoreCase(cleanText)) {
                return rol;
            }
        }
        try {
            return Role.valueOf(cleanText.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No se encontró ningún rol válido para el texto: '" + text + "'");
        }
    }

}