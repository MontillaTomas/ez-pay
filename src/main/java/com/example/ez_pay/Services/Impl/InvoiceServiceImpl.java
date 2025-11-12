package com.example.ez_pay.Services.Impl;

import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Mappers.InvoiceMapper;
import com.example.ez_pay.Models.Invoice;
import com.example.ez_pay.Repositories.InvoiceRepository;
import com.example.ez_pay.Services.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;


    @Override
    @Transactional(readOnly = true)
    public Optional<InvoiceResponse> getInvoiceById(UUID id) {
        return invoiceRepository.findById(id)
                .map(invoiceMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByCompanyId(Long companyId) {
        List<Invoice> invoices = invoiceRepository.findByCompanyCompanyId(companyId);
        return invoiceMapper.toResponseList(invoices);
    }

    @Override
    public InvoiceResponse createInvoice(Invoice invoice) {
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
