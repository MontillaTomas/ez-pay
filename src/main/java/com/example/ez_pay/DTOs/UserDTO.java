package com.example.ez_pay.DTOs;

import com.example.ez_pay.Models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "RegistroUsuarioDTO",
        description = "DTO que contiene los datos necesarios para registrar un nuevo usuario (Empleado o Empresa)."
)
public class UserDTO {
    @Schema(
            description = "Nombre de pila del usuario.",
            example = "Juan"
    )
    private String firstname;

    @Schema(
            description = "Apellido del usuario.",
            example = "Pérez"
    )
    private String lastName;

    @Schema(
            description = "Fecha de nacimiento del usuario.",
            example = "1995-05-20"
    )
    private LocalDate birth;

    @Schema(
            description = "Nombre de usuario único para el inicio de sesión.",
            example = "jperez95"
    )
    private String username;

    @Schema(
            description = "Contraseña en texto plano. La API se encargará de hashearla.",
            example = "unaClaveMuySegura123"
    )
    private String password;

    @Schema(
            description = "Dirección de correo electrónico única del usuario.",
            example = "juan.perez@dominio.com"
    )
    private String email;

    @Schema(
            description = "Número de teléfono de contacto.",
            example = "1122334455"
    )
    private String phone;

    @Schema(
            description = "Rol que tendrá el usuario en la plataforma.",
            example = "Empleado",
            allowableValues = {"Empleado", "Empresa"}
    )
    private String rol;
}