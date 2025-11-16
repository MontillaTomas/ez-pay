package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.AuthResponseDTO;
import com.example.ez_pay.DTOs.LoginRequestDTO;
import com.example.ez_pay.DTOs.ResponseDTO;
import com.example.ez_pay.DTOs.UserDTO;
import com.example.ez_pay.Services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Autenticación",
        description = "Endpoints para el registro de usuarios (Sign Up) e inicio de sesión (Sign In)."
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una nueva cuenta de usuario (ej. EMPLEADO o EMPRESA). Valida los detalles provistos, hashea la contraseña y guarda el nuevo usuario en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de creación exitosa",
                                    value = "{\"status\": \"201\", \"message\": \"User created successfully\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error en la solicitud. (Ver ejemplos para detalles)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Usuario registrado",
                                            value = "{\"status\": \"400\", \"message\": \"Error: El nombre de usuario ya está en uso.\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Rol no válido",
                                            value = "{\"status\": \"400\", \"message\": \"Error: El rol ingresado '[rol_ingresado]' no es válido\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al procesar el registro."
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody UserDTO request) {
        authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO("201", "User created successfully"));

    }

    @Operation(
            summary = "Autenticar un usuario (Sign In)",
            description = "Valida las credenciales de un usuario (nombre de usuario y contraseña). Si es exitoso, devuelve un JWT (JSON Web Token) para ser usado en la autorización de solicitudes subsecuentes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Devuelve el token JWT en formato String.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)) }
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario o contraseña incorrectos"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al intentar la autenticación."
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(authService.login(request));
    }
}
