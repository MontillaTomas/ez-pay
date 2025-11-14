package com.example.ez_pay.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(
        name = "LoginDTO",
        description = "DTO que contiene los datos necesarios para iniciar sesión en la plataforma. Primero debe registrarse para poder iniciar sesión"
)
public class LoginRequestDTO {
    @Schema(
            description = "Nombre de usuario EXISTENTE para el inicio de sesión.",
            example = "jperez95"
    )
    private String username;

    @Schema(
            description = "Contraseña en texto plano",
            example = "jperez95"
    )
    private String password;
}
