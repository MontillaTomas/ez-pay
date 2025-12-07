package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Response.InvoiceResponse;
import com.example.ez_pay.Services.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Controlador de Facturas", description = "Endpoints para creación y consulta de facturas")
@SecurityRequirement(name = "bearerAuth")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Operation(
            summary = "Obtener facturas de la empresa autenticada",
            description = "Devuelve una página de facturas pertenecientes a la empresa asociada al usuario autenticado. Requiere autenticación. Parámetros: page (número de página, base 0), size (tamaño de página)."
    )
    @GetMapping
    public ResponseEntity<Page<InvoiceResponse>> getInvoicesForAuthenticatedUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InvoiceResponse> invoices = invoiceService.getInvoicesForAuthenticatedUser(page, size);
        return ResponseEntity.ok(invoices);
    }

    @Operation(
            summary = "Obtener factura por ID",
            description = "Recupera los detalles de una factura dado su ID. Requiere autenticación."
    )
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getInvoiceById(id));
    }

    @Operation(
            summary = "Obtener factura por Código Electrónico de Pago",
            description = "Recupera los detalles de una factura dado su Código Electrónico de Pago. No requiere autenticación."
    )
    @GetMapping("epc/{epc}")
    public ResponseEntity<InvoiceResponse> getInvoiceByEpc(@PathVariable String epc) {
        return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getInvoiceByEpc(epc));
    }

    @Operation(
            summary = "Crear nueva factura",
            description = "Crea una nueva factura asociada a la empresa del usuario autenticado."
    )
    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceCreateRequest invoice) {
        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }
}
