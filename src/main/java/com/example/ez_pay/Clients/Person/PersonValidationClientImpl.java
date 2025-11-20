package com.example.ez_pay.Clients.Person;

import com.example.ez_pay.Clients.Token.ApiTokenService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;

@Component
public class PersonValidationClientImpl implements PersonValidationClient {
    private final RestTemplate restTemplate;
    private final ApiTokenService apiTokenService;

    public PersonValidationClientImpl(RestTemplate restTemplate, ApiTokenService apiTokenService) {
        this.restTemplate = restTemplate;
        this.apiTokenService = apiTokenService;
    }


    @Override
    public boolean validatePerson(String identifier) {
        String token = apiTokenService.getApiToken();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-Key", token);

        HttpEntity<Void> req = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.exchange(
                "https://api.com/validate/" + identifier,
                HttpMethod.GET,
                req,
                Boolean.class
        );

        apiTokenService.registerUsage();

        return Boolean.TRUE.equals(response.getBody());
    }
}
