package com.example.ez_pay.Formatters;

import com.example.ez_pay.Models.Company;
import org.springframework.stereotype.Component;

@Component
public class ServiceNumberEPCFormatter implements EPCFormatter<Company> {
    @Override
    public String format(Company company) {
        if (company == null) return "0000";
        String serviceNumber = String.valueOf(company.getCompanyId());
        if (serviceNumber.length() > 4) {
            serviceNumber = serviceNumber.substring(serviceNumber.length() - 4);
        }
        return String.format("%4s", serviceNumber).replace(' ', '0');
    }
}
