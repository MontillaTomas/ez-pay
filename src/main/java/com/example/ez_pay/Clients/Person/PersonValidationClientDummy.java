package com.example.ez_pay.Clients.Person;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class PersonValidationClientDummy implements PersonValidationClient{
    @Override
    public boolean validatePerson(String identifier) {
        return true;
    }
}
