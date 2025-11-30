package com.example.ez_pay.DTOs.Request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        name = "InvoiceCreateRequest",
        description = """
                DTO para la creación de una factura.
                Contiene los datos mínimos y opcionales que la API valida al crear una nueva factura.
                * En caso de no proveer ambas fechas de vencimiento, la factura siempre podrá ser pagada.
                * Si solo se provee `dueDate`, la factura podrá ser pagada hasta esa fecha.
                * Si se proveen ambas fechas, la factura podrá ser pagada hasta `secondDueDate`.
                """
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceCreateRequest {

    @Schema(
            description = "Nombre o razón social del receptor de la factura. Se limpia de espacios en blanco en el backend.",
            example = "Empresa SA",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Nombre del receptor es requerido")
    private String receiverName;

    @Schema(
            description = "CUIL del receptor. Debe ser exactamente 11 dígitos numéricos.",
            example = "20333444555",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "CUIL del receptor es requerido")
    @Pattern(regexp = "\\d{11}", message = "CUIL must be exactly 11 digits")
    private String receiverCUIL;

    @Schema(
            description = "Identificador de cliente (14 dígitos). Debe ser único dentro de la misma empresa.",
            example = "12345678901234",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Identificador de cliente es requerido")
    @Pattern(regexp = "\\d{14}", message = "Client identifier must be exactly 14 digits")
    private String clientIdentifier;

    @Schema(
            description = "Monto principal de la factura. Debe ser un número mayor que cero.",
            example = "1500.50",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Monto es requerido")
    @Positive(message = "Monto debe ser mayor que cero")
    private BigDecimal amount;

    @Schema(
            description = "Segundo monto. Si se provee, debe ser mayor que cero.",
            example = "500.00"
    )
    @Positive(message = "Segundo monto debe ser mayor que cero")
    private BigDecimal secondAmount;

    @Schema(
            description = "Fecha de vencimiento del primer pago. No puede estar en el pasado si se provee.",
            example = "2025-12-31"
    )
    private LocalDate dueDate;

    @Schema(
            description = "Fecha de vencimiento del segundo pago (opcional). Si se provee, debe ser posterior a `dueDate`.",
            example = "2026-01-31"
    )
    private LocalDate secondDueDate;

    @Schema(
            description = "Fecha de emisión de la factura.",
            example = "2025-11-19",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Fecha de emisión es requerida")
    private LocalDate issueDate;
}
