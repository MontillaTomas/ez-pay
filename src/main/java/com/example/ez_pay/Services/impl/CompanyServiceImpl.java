package com.example.ez_pay.Services.impl;

import com.example.ez_pay.DTOs.CompanyDTO;
import com.example.ez_pay.Models.Category;
import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.messaging.CompanyQueueManager;
import com.example.ez_pay.Services.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private final CompanyQueueManager companyQueueManager;

    @Override
    public void createCompany(CompanyDTO companyDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un usuario autenticado.");
        }

        String ownerUsername = authentication.getName();

        UserEntity owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (companyRepository.findByUserId(owner.getId()).isPresent()) {
            throw new RuntimeException("Ya existe un compania para el usuario. Un usuario solo puede tener una empresa asociada");
        }

        if (companyRepository.findByCuit(companyDTO.getCuit()).isPresent()) {
            throw new RuntimeException("Error: ya existe una empresa con el CUIT ingresado");
        }
        Category requestedCategory = companyDTO.getCategory();

        if (requestedCategory == null ) {
            throw new RuntimeException("Error: Debe especificar una categoría.");
        }

        Company company = new Company(requestedCategory, companyDTO.getAddress(), companyDTO.getProvince(), companyDTO.getCity(), companyDTO.getMonthlyInvoices(), companyDTO.getCuit(), companyDTO.getLegalName(), companyDTO.getNumberOfPayments(),companyDTO.getAverageInvoice(), owner);

        company = companyRepository.save(company);

        companyQueueManager.registerCompanyQueue(company.getCompanyId());
    }
}
