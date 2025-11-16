package com.example.ez_pay.Mappers;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Request.InvoiceUpdateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Models.Company;
import com.example.ez_pay.Models.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {
    public InvoiceResponse toResponse(Invoice entity) {
        if (entity == null) {
            return null;
        }

        InvoiceResponse dto = new InvoiceResponse();
        dto.setId(entity.getId());
        dto.setCompanyId(entity.getCompany().getCompanyId());
        dto.setReceiverName(entity.getReceiverName());
        dto.setReceiverCUIL(entity.getReceiverCUIL());
        dto.setAmount(entity.getAmount());
        dto.setCreationDate(entity.getCreationDate());
        dto.setExpirationDate(entity.getExpirationDate());

        return dto;
    }

    public List<InvoiceResponse> toResponseList(List<Invoice> entities) {
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public Page<InvoiceResponse> toResponsePage(Page<Invoice> page) {
        if (page == null) {
            return Page.empty();
        }
        List<InvoiceResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }

    public Invoice toEntity(InvoiceCreateRequest invoiceRequest, Company company) {
        if (invoiceRequest == null) {
            return null;
        }

        Invoice invoice = new Invoice();
        invoice.setCompany(company);
        invoice.setReceiverName(invoiceRequest.getReceiverName());
        invoice.setReceiverCUIL(invoiceRequest.getReceiverCUIL());
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setExpirationDate(invoiceRequest.getExpirationDate());
        invoice.setCreationDate(invoiceRequest.getCreationDate());
        return invoice;
    }

    public Invoice toEntity(InvoiceUpdateRequest invoiceRequest, Company company) {
        if (invoiceRequest == null) {
            return null;
        }

        Invoice invoice = new Invoice();
        invoice.setCompany(company);
        invoice.setReceiverName(invoiceRequest.getReceiverName());
        invoice.setReceiverCUIL(invoiceRequest.getReceiverCUIL());
        invoice.setAmount(invoiceRequest.getAmount());
        invoice.setExpirationDate(invoiceRequest.getExpirationDate());
        invoice.setCreationDate(invoiceRequest.getCreationDate());
        return invoice;
    }
}
