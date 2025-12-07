package com.example.ez_pay.DTOs.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(
        name = "InvoicePaymentRequest",
        description = "DTO para el pago de una factura."
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePaymentRequest {
    @Schema(
            description = "Código de pago electrónico (CPE) de la factura a pagar.",
            example = "000101520075253342033344455612300000000016",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "CPE es requerido")
    private String epc;

    @Schema(
            description = "Monto recibido para el pago de la factura.",
            example = "1000.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Monto recibido es requerido")
    private BigDecimal amountReceived;
}
