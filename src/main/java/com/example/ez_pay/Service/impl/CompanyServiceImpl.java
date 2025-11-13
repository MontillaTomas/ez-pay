package com.example.ez_pay.Service.impl;

import com.example.ez_pay.DTO.CompanyDTO;
import com.example.ez_pay.Model.Category;
import com.example.ez_pay.Model.Company;
import com.example.ez_pay.Model.UserEntity;
import com.example.ez_pay.Repository.CompanyRepository;
import com.example.ez_pay.Repository.UserRepository;
import com.example.ez_pay.Service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    @Override
    public void createCompany(CompanyDTO companyDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un usuario autenticado.");
        }

        String ownerUsername = authentication.getName();

        UserEntity owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Category requestedCategory = companyDTO.getCategory();

        if (requestedCategory == null ) {
            throw new RuntimeException("Error: Debe especificar una categoría.");
        }

        Company company = new Company(requestedCategory, companyDTO.getAddress(), companyDTO.getProvince(), companyDTO.getCity(), companyDTO.getMonthlyInvoices(), companyDTO.getCuit(), companyDTO.getLegalName(), companyDTO.getNumberOfPayments(),companyDTO.getAverageInvoice(), owner);

        companyRepository.save(company);
    }
}
