package com.example.ez_pay.Services.impl;

import com.example.ez_pay.DTOs.CompanyDTO;
import com.example.ez_pay.Mappers.CompanyMapper;
import com.example.ez_pay.Models.Category;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.CompanyService;
import com.example.ez_pay.Services.IAfipValidationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private CompanyMapper companyMapper;
    private IAfipValidationService afipValidationService;

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
        if (!validateCuitFormat(companyDTO.getCuit())) {
            throw new IllegalArgumentException("Error: invalid cuit format");
        }
        if (!afipValidationService.isCuitValid(companyDTO.getCuit())) {
            throw new ResourceNotFoundException("Error: El CUIT '" + companyDTO.getCuit() + "' no es válido o no existe en el padrón de ARCA.");
        }
        companyRepository.save(companyMapper.toEntity(companyDTO));
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

    private boolean validateCuitFormat(String cuit) {
        String cuitLimpio = cuit.replace("-", "").trim();

        //verifica que sean 11
        if (!cuitLimpio.matches("^\\d{11}$")) {
            return false;
        }

        // 3. Aplicar el algoritmo Módulo 11

        // Array de multiplicadores
        int[] multiplicadores = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

        int suma = 0;

        try {
            for (int i = 0; i < 10; i++) {
                int digito = Character.getNumericValue(cuitLimpio.charAt(i));
                suma += digito * multiplicadores[i];
            }

            // Calculamos el resto y el dígito verificador esperado
            int resto = suma % 11;
            int digitoEsperado = 11 - resto;

            // Aplicamos las reglas especiales
            if (digitoEsperado == 11) {
                digitoEsperado = 0;
            } else if (digitoEsperado == 10) {
                digitoEsperado = 9;
            }

            // Obtenemos el dígito verificador real del CUIT
            int digitoReal = Character.getNumericValue(cuitLimpio.charAt(10));

            // Comparamos el dígito esperado con el real
            return digitoEsperado == digitoReal;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
