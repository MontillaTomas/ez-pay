package com.example.ez_pay.Services.impl;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Exceptions.AccessDeniedException;
import com.example.ez_pay.Exceptions.NotAuthenticatedException;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Mappers.InvoiceMapper;
import com.example.ez_pay.Models.*;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.InvoiceRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.InvoiceService;
import com.example.ez_pay.Services.PaymentStubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final InvoiceMapper invoiceMapper;
    private final UserRepository userRepository;
    private final PaymentStubService paymentStubService;

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
        validateSecondAmount(invoiceRequest.getSecondAmount());
        validateDueDates(invoiceRequest.getDueDate(), invoiceRequest.getSecondDueDate());


        // Clean receiver name
        String cleanedName = cleanReceiverName(invoiceRequest.getReceiverName());
        invoiceRequest.setReceiverName(cleanedName);

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, companyOpt.get());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setPaymentStub(paymentStubService.generatePaymentStub(invoice));
        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    private String cleanReceiverName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim();
    }

    private void validateCUIL(String cuil) {
        if (cuil == null || !cuil.matches("\\d{11}")) {
            throw new IllegalArgumentException("CUIL must be exactly 11 digits.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.doubleValue() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private void validateSecondAmount(BigDecimal amount) {
        if (amount != null && amount.doubleValue() <= 0) {
            throw new IllegalArgumentException("Second amount, if provided, must be greater than zero.");
        }
    }

    private void validateDueDates(java.time.LocalDate dueDate, java.time.LocalDate secondDueDate) {
        if (secondDueDate != null && !secondDueDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Second due date must be after the first due date.");
        }

        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past.");
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
