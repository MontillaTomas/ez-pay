package com.example.ez_pay.Controller;

import com.example.ez_pay.DTO.AuthResponseDTO;
import com.example.ez_pay.DTO.LoginRequestDTO;
import com.example.ez_pay.DTO.UserDTO;
import com.example.ez_pay.Services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
                    responseCode = "200",
                    description = "¡Usuario registrado exitosamente!"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error: El nombre de usuario ya está en uso."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error: El rol especificado no es válido. Debe ser EMPLEADO o EMPRESA."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al procesar el registro."
            )
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("¡Usuario registrado exitosamente!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
                    responseCode = "401",
                    description = "Autenticación fallida: nombre de usuario o contraseña incorrectos."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor al intentar la autenticación."
            )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        try {
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDTO(null));
        }
    }
}
