package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Request.InvoiceUpdateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<Page<InvoiceResponse>> getInvoicesForAuthenticatedUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InvoiceResponse> invoices = invoiceService.getInvoicesForAuthenticatedUser(page, size);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getInvoiceById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<InvoiceResponse>> getInvoicesByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InvoiceResponse> invoices = invoiceService.getInvoicesByCompanyId(companyId, page, size);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceCreateRequest invoice) {
        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable UUID id, @RequestBody InvoiceUpdateRequest invoiceRequest) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id, invoiceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedInvoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
