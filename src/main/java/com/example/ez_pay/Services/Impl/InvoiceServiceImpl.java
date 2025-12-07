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
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el nombre: " + username));

        Company company = companyRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada para el usuario con ID: " + user.getId()));

        PageRequest pageable = PageRequest.of(page, size);
        Page<Invoice> invoicePage = invoiceRepository.findByCompanyCompanyId(company.getCompanyId(), pageable);
        return invoiceMapper.toResponsePage(invoicePage);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(UUID id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada para el ID: " + id));

        // Ensure the authenticated user owns the invoice's company
        String username = getAuthenticatedUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el nombre: " + username));

        Company invoiceCompany = invoice.getCompany();
        if (invoiceCompany == null || invoiceCompany.getUser() == null || !invoiceCompany.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("El usuario no tiene permiso para acceder a esta factura.");
        }

        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public InvoiceResponse getInvoiceByEpc(String epc) {
        validateEPC(epc);
        Invoice invoice = invoiceRepository.findByPaymentStubElectronicPaymentCode(epc)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada para el CEP: " + epc));
        return invoiceMapper.toResponse(invoice);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public InvoiceResponse createInvoice(InvoiceCreateRequest invoiceRequest) {
        // Resolve authenticated user and company
        String username = getAuthenticatedUsername();
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el nombre: " + username));

        Optional<Company> companyOpt = companyRepository.findByUserId(user.getId());
        if (companyOpt.isEmpty()) {
            throw new ResourceNotFoundException("Empresa no encontrada para el usuario con ID: " + user.getId());
        }

        // Validate inputs using private helpers
        validateCUIL(invoiceRequest.getReceiverCUIL());
        validateClientIdentifier(invoiceRequest.getClientIdentifier());
        validateAmount(invoiceRequest.getAmount());
        validateSecondAmount(invoiceRequest.getSecondAmount(), invoiceRequest.getSecondDueDate());
        validateDueDates(invoiceRequest.getDueDate(), invoiceRequest.getSecondDueDate());

        // Check if the client identifier is unique for this company
        Optional<Invoice> existingInvoiceOpt = invoiceRepository.findByClientIdentifierAndCompanyCompanyId(
                invoiceRequest.getClientIdentifier(), companyOpt.get().getCompanyId());
        if (existingInvoiceOpt.isPresent()) {
            throw new IllegalArgumentException("Una factura con el mismo identificador de cliente ya existe para esta empresa.");
        }

        // Clean receiver name
        String cleanedName = cleanReceiverName(invoiceRequest.getReceiverName());
        invoiceRequest.setReceiverName(cleanedName);

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, companyOpt.get());
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setPaymentStub(paymentStubService.generatePaymentStub(invoice));
        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    private void validateEPC(String epc) {
        if (epc == null || !epc.matches("\\d{42}")) {
            throw new IllegalArgumentException("Código Electrónico de Pago debe tener exactamente 42 dígitos.");
        }
    }

    private String cleanReceiverName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim();
    }

    private void validateClientIdentifier(String clientIdentifier) {
        if (clientIdentifier == null || !clientIdentifier.matches("\\d{14}")) {
            throw new IllegalArgumentException("Identificador del cliente debe tener exactamente 14 dígitos.");
        }
    }

    private void validateCUIL(String cuil) {
        if (cuil == null || !cuil.matches("\\d{11}")) {
            throw new IllegalArgumentException("CUIL debe tener exactamente 11 dígitos.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.doubleValue() <= 0) {
            throw new IllegalArgumentException("Monto debe ser mayor que cero.");
        }
    }

    private void validateSecondAmount(BigDecimal amount, LocalDate secondDueDate) {
        if (amount != null && amount.doubleValue() <= 0) {
            throw new IllegalArgumentException("Segundo monto debe ser mayor que cero si se proporciona.");
        }
        if (secondDueDate != null && amount == null) {
            throw new IllegalArgumentException("Segundo monto no puede ser nulo si se proporciona la segunda fecha de vencimiento.");
        }
    }

    private void validateDueDates(java.time.LocalDate dueDate, java.time.LocalDate secondDueDate) {
        if (dueDate == null && secondDueDate == null) {
            return;
        }
        if (dueDate == null && secondDueDate != null) {
            throw new IllegalArgumentException("Primera fecha de vencimiento no puede ser nula si se proporciona la segunda fecha de vencimiento.");
        }
        if (secondDueDate != null && !secondDueDate.isAfter(dueDate)) {
            throw new IllegalArgumentException("Segunda fecha de vencimiento debe ser posterior a la primera fecha de vencimiento.");
        }

        if (dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Primera fecha de vencimiento no puede ser una fecha pasada.");
        }
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException("Usuario no autenticado.");
        }
        return authentication.getName();
    }
}
