package com.example.ez_pay.Mappers;

import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Models.Invoice;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
