package com.example.ez_pay.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Respuesta existosa de los endpoints",
        description = "DTO para almacenar información estándar de respuesta de la API."
)
public class ResponseDTO {
    @Schema(
            description = "El código de estado HTTP de la respuesta.",
            example = "200"
    )
    private String status;
    @Schema(
            description = "Un mensaje descriptivo detallando el estado de la respuesta.",
            example = "Operación completada exitosamente."
    )
    private String message;
}
