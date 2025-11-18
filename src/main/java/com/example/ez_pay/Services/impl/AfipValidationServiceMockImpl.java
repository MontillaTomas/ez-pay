package com.example.ez_pay.Services.impl;

import com.example.ez_pay.Services.IAfipValidationService;
import org.springframework.stereotype.Service;

@Service
public class AfipValidationServiceMockImpl implements IAfipValidationService {
    @Override
    public boolean isCuitValid(String cuit) {
        if (cuit == null || cuit.trim().isEmpty() || cuit.equals("00-00000000-0")) {
            return false;
        }
        return true;
    }



}
