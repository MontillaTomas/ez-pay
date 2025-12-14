package com.example.ez_pay.Mappers;

import com.example.ez_pay.DTOs.CompanyDTO;
import com.example.ez_pay.Models.Category;
import com.example.ez_pay.Models.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public CompanyDTO toDTO(Company company){
        if (company == null) {
            return null;
        }

        CompanyDTO dto = new CompanyDTO();

        dto.setAddress(company.getAddress());
        dto.setProvince(company.getProvince());
        dto.setCity(company.getCity());
        dto.setMonthlyInvoices(company.getMonthlyInvoices());
        dto.setCuit(company.getCuit());
        dto.setLegalName(company.getLegalName());
        dto.setNumberOfPayments(company.getNumberOfPayments());
        dto.setAverageInvoice(company.getAverageInvoice());

        return dto;
    }

    public Company toEntity(CompanyDTO companyDTO){
        if (companyDTO == null) {
            return null;
        }

        Company company = new Company();

        if (companyDTO.getCategory() != null && !companyDTO.getCategory().isEmpty()) {
            company.setCategory(Category.fromText(companyDTO.getCategory()));
        }

        company.setAddress(companyDTO.getAddress());
        company.setProvince(companyDTO.getProvince());
        company.setCity(companyDTO.getCity());
        company.setMonthlyInvoices(companyDTO.getMonthlyInvoices());
        company.setCuit(companyDTO.getCuit());
        company.setLegalName(companyDTO.getLegalName());
        company.setNumberOfPayments(companyDTO.getNumberOfPayments());
        company.setAverageInvoice(companyDTO.getAverageInvoice());

        return company;

    }


}