package com.example.ez_pay.Services;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface InvoiceService {
    Page<InvoiceResponse> getInvoicesForAuthenticatedUser(int page, int size);

    InvoiceResponse getInvoiceById(UUID id);

    InvoiceResponse createInvoice(InvoiceCreateRequest invoiceRequest);
}
