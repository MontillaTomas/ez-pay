package com.example.ez_pay.Services.impl;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Exceptions.AccessDeniedException;
import com.example.ez_pay.Exceptions.NotAuthenticatedException;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Mappers.InvoiceMapper;
import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Models.InvoiceStatus;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.InvoiceRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final InvoiceMapper invoiceMapper;
    private final UserRepository userRepository;

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<InvoiceResponse> getInvoicesForAuthenticatedUser(int page, int size) {
        String username = getAuthenticatedUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found for user id: " + user.getId()));

        PageRequest pageable = PageRequest.of(page, size);
        Page<Invoice> invoicePage = invoiceRepository.findByCompanyCompanyId(company.getCompanyId(), pageable);
        return invoiceMapper.toResponsePage(invoicePage);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        // Ensure the authenticated user owns the invoice's company
        String username = getAuthenticatedUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Company invoiceCompany = invoice.getCompany();
        if (invoiceCompany == null || invoiceCompany.getUser() == null || !invoiceCompany.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User is not the owner of this invoice and cannot update it.");
        }

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest invoiceRequest) {
        // Resolve authenticated user and company
        String username = getAuthenticatedUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Optional<Company> companyOpt = companyRepository.findByUserId(user.getId());
        if (companyOpt.isEmpty()) {
            throw new ResourceNotFoundException("Company not found for user id: " + user.getId());
        }

        // Validate inputs using private helpers
        validateCUIL(invoiceRequest.getReceiverCUIL());
        validateAmount(invoiceRequest.getAmount());

        // Clean receiver name
        invoiceRequest.setReceiverName(invoiceRequest.getReceiverName().trim());

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, companyOpt.get());
        invoice.setStatus(InvoiceStatus.PENDING);
        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    private void validateCUIL(String cuil) {
        if (cuil == null || cuil.length() != 11) {
            throw new IllegalArgumentException("CUIL must be exactly 11 characters long.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.doubleValue() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException("No authenticated user found.");
        }
        return authentication.getName();
    }
}
