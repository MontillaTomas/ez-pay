package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return invoiceService.getInvoiceById(id)
                .map(invoiceResponse -> ResponseEntity.ok().body(invoiceResponse))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<InvoiceResponse>> getInvoicesByCompanyId(@PathVariable Long companyId) {
        List<InvoiceResponse> invoices = invoiceService.getInvoicesByCompanyId(companyId);
        return ResponseEntity.ok(invoices);
    }
}
