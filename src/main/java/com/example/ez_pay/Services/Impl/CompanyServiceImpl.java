
package com.example.ez_pay.Services.Impl;

import com.example.ez_pay.DTOs.CompanyDTO;
import com.example.ez_pay.Mappers.CompanyMapper;
import com.example.ez_pay.Models.Category;
import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.CompanyService;
import com.example.ez_pay.Services.messaging.CompanyQueueManager;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private CompanyMapper companyMapper;
    private final CompanyQueueManager companyQueueManager;

    @Override
    public void createCompany(CompanyDTO companyDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not logged in");
        }

        String ownerUsername = authentication.getName();

        UserEntity owner = userRepository.findByUsername(ownerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (companyRepository.findByUserId(owner.getId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un compania para el usuario. Un usuario solo puede tener una empresa asociada");
        }

        if (companyRepository.findByCuit(companyDTO.getCuit()).isPresent()) {
            throw new IllegalArgumentException("Error: ya existe una empresa con el CUIT ingresado");
        }
        validateCategory(companyDTO.getCategory());
        Company company = companyMapper.toEntity(companyDTO);
        company.setUser(owner);
        companyRepository.save(company);
        //companyQueueManager.registerCompanyQueue(company.getCompanyId());
    }

    private Category validateCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: Debe especificar una categoría.");
        }

        try {
            return Category.fromText(categoryName.trim());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error: la categoría '" + categoryName + "' no es un valor válido.");
        }
    }
}
