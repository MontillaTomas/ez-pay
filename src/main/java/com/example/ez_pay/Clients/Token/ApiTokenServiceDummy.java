package com.example.ez_pay.Clients.Token;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class ApiTokenServiceDummy implements ApiTokenService {
    @Override
    public String getApiToken() {
        return "dummy-token";
    }

    @Override
    public void registerUsage() {
        // No operation needed
    }
}
