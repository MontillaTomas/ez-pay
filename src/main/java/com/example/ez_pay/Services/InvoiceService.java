package com.example.ez_pay.Services;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Models.Invoice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse getInvoiceById(UUID id);

    List<InvoiceResponse> getInvoicesByCompanyId(Long companyId);

    InvoiceResponse createInvoice(InvoiceCreateRequest invoice);

    InvoiceResponse updateInvoice(UUID id, Invoice invoice);

    void deleteInvoice(UUID id);
}
