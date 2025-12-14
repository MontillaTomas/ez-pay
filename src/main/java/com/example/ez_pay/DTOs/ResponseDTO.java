package com.example.ez_pay.DTOs;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "ResponseDTO",
        description = "DTO genérico para respuestas de la API, usualmente para errores o mensajes de estado."
)
public class ResponseDTO {
    @Schema(
            description = "El código de estado HTTP de la respuesta.",
            example = "404"
    )
    private String status;
    @Schema(
            description = "Un mensaje descriptivo detallando el resultado de la operación.",
            example = "Recurso no encontrado."
    )
    private String message;
}