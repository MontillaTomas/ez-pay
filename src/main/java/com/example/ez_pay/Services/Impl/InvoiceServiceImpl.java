package com.example.ez_pay.Services.Impl;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Mappers.InvoiceMapper;
import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.InvoiceRepository;
import com.example.ez_pay.Services.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private final InvoiceMapper invoiceMapper;


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
    public InvoiceResponse createInvoice(InvoiceCreateRequest invoiceRequest) {
        // Validate cuil length
        if (invoiceRequest.getReceiverCUIL().length() != 11) {
            throw new IllegalArgumentException("CUIL must be exactly 11 characters long.");
        }
        // Amount can't be less than or equal to zero
        if (invoiceRequest.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        // TODO: Get the company from the database using the user id from the token
        Long companyId = 1L; // Placeholder for actual company ID retrieval logic
        Optional<Company> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isEmpty()) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId);
        }
        // Clean receiver name
        invoiceRequest.setReceiverName(invoiceRequest.getReceiverName().trim());

        Invoice invoice = invoiceMapper.toEntity(invoiceRequest, companyOpt.get());
        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    @Override
    public InvoiceResponse updateInvoice(UUID id, Invoice invoice) {
        Invoice existing = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found with id: " + id));

        existing.setReceiverName(invoice.getReceiverName());
        existing.setReceiverCUIL(invoice.getReceiverCUIL());
        existing.setAmount(invoice.getAmount());
        existing.setExpirationDate(invoice.getExpirationDate());
        if (invoice.getCompany() != null) {
            existing.setCompany(invoice.getCompany());
        }

        Invoice saved = invoiceRepository.save(existing);
        return invoiceMapper.toResponse(saved);
    }

    @Override
    public void deleteInvoice(UUID id) {
        invoiceRepository.deleteById(id);
    }
}
