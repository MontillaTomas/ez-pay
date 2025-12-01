package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.Request.InvoicePaymentRequest;
import com.example.ez_pay.DTOs.Response.InvoicePaymentResponse;
import com.example.ez_pay.Services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Controlador de Pagos", description = "Endpoints para realizar pagos")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(
            summary = "Realizar el pago de una factura",
            description = "Registra el pago de una factura utilizando su Código Electrónico de Pago (EPC). Debe estar autenticado un usuario con el rol de EMPLEADO."
    )
    @PostMapping
    public ResponseEntity<InvoicePaymentResponse> makePayment(@Valid @RequestBody InvoicePaymentRequest request) {
        InvoicePaymentResponse response = paymentService.processInvoicePayment(request);
        return ResponseEntity.ok(response);
    }
}
