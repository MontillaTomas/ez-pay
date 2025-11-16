package com.example.ez_pay.Services.impl;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Request.InvoiceUpdateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Exceptions.NotAuthenticatedException;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Mappers.InvoiceMapper;
import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.InvoiceRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
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
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByCompanyId(Long companyId) {
        List<Invoice> invoices = invoiceRepository.findByCompanyCompanyId(companyId);
        return invoiceMapper.toResponseList(invoices);
    }

    @Override
    @Transactional
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
        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public InvoiceResponse updateInvoice(UUID id, InvoiceUpdateRequest invoice) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));

        // Ensure the authenticated user owns the invoice's company
        String username = getAuthenticatedUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        Company invoiceCompany = existingInvoice.getCompany();
        if (invoiceCompany == null || invoiceCompany.getUser() == null || !invoiceCompany.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User is not the owner of this invoice and cannot update it.");
        }

        // Validate inputs using private helpers
        validateCUIL(invoice.getReceiverCUIL());
        validateAmount(invoice.getAmount());

        // Clean receiver name
        invoice.setReceiverName(invoice.getReceiverName().trim());

        Invoice updatedEntity = invoiceMapper.toEntity(invoice, existingInvoice.getCompany());
        updatedEntity.setId(existingInvoice.getId()); // Preserve the original ID
        Invoice updatedInvoice = invoiceRepository.save(updatedEntity);
        return invoiceMapper.toResponse(updatedInvoice);
    }

    @Override
    @Transactional
    public void deleteInvoice(UUID id) {
        if (!invoiceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found with id: " + id);
        }

        invoiceRepository.deleteById(id);
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
