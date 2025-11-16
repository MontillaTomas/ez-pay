package com.example.ez_pay.Services;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Request.InvoiceUpdateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse getInvoiceById(UUID id);

    Page<InvoiceResponse> getInvoicesByCompanyId(Long companyId, int page, int size);

    InvoiceResponse createInvoice(InvoiceCreateRequest invoice);

    InvoiceResponse updateInvoice(UUID id, InvoiceUpdateRequest invoice);

    void deleteInvoice(UUID id);

    Page<InvoiceResponse> getInvoicesForAuthenticatedUser(int page, int size);
}
