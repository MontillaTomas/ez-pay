package com.example.ez_pay.Config;

import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Services.messaging.CompanyQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QueueInitializer implements ApplicationRunner {

    private final CompanyRepository companyRepository;
    private final CompanyQueueManager companyQueueManager;

    @Override
    public void run(ApplicationArguments args) {
        List<Company> companies = companyRepository.findAll();

        for (Company company : companies) {
            companyQueueManager.registerCompanyQueue(company.getCompanyId());
        }
    }
}

