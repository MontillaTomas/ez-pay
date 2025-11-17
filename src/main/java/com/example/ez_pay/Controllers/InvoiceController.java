package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.Request.InvoiceCreateRequest;
import com.example.ez_pay.DTOs.Request.InvoiceUpdateRequest;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Controlador de Facturas", description = "Endpoints para creación, consulta, actualización y eliminación de facturas")
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
            summary = "Obtener facturas por empresa",
            description = "Devuelve una página de facturas de la empresa indicada por companyId. Requiere autenticación. Parámetros: page (número de página, base 0), size (tamaño de página)."
    )
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<InvoiceResponse>> getInvoicesByCompanyId(
            @PathVariable Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<InvoiceResponse> invoices = invoiceService.getInvoicesByCompanyId(companyId, page, size);
        return ResponseEntity.ok(invoices);
    }

    @Operation(
            summary = "Crear nueva factura",
            description = "Crea una nueva factura asociada a la empresa del usuario autenticado."
    )
    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceCreateRequest invoice) {
        InvoiceResponse createdInvoice = invoiceService.createInvoice(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
    }

    @Operation(
            summary = "Actualizar factura",
            description = "Actualiza una factura existente; sólo el usuario propietario de la empresa asociada a la factura puede actualizarla."
    )
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponse> updateInvoice(@PathVariable UUID id, @RequestBody InvoiceUpdateRequest invoiceRequest) {
        InvoiceResponse updatedInvoice = invoiceService.updateInvoice(id, invoiceRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedInvoice);
    }

    @Operation(
            summary = "Eliminar factura",
            description = "Elimina una factura por su ID. Devuelve 204 No Content si la eliminación fue exitosa o 404 si no se encontró la factura."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
