package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.CompanyDTO;
import com.example.ez_pay.DTOs.ResponseDTO;
import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Models.UserEntity;
import com.example.ez_pay.Repositories.CompanyRepository;
import com.example.ez_pay.Repositories.UserRepository;
import com.example.ez_pay.Services.AfipValidationService;
import com.example.ez_pay.Services.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(
        name = "Empresa",
        description = "Endpoint para el registro de empresas"
)
public class CompanyController {
    private final CompanyService companyService;
    private final AfipValidationService afipValidationService;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Operation(
            summary = "Registrar una nueva empresa",
            description = "Crea una nueva empresa. Para ello, el usuario debe estar creado y autenticado con el ROL='Empresa'"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Company created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo de creación exitosa",
                                    value = "{\"status\": \"201\", \"message\": \"Company created successfully\"}"
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
                                            name = "Usuario ya tiene empresa", // Nombre para el ejemplo
                                            value = "{\"status\": \"400\", \"message\": \"Ya existe un compania para el usuario. Un usuario solo puede tener una empresa asociada\"}"
                                    ),
                                    @ExampleObject(
                                            name = "CUIT duplicado", // Nombre para el ejemplo
                                            value = "{\"status\": \"400\", \"message\": \"Error: ya existe una empresa con el CUIT ingresado\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Categoría no válida", // Nombre para el ejemplo
                                            value = "{\"status\": \"400\", \"message\": \"Error: la categoría '[valor_ingresado]' no es un valor válido\"}"
                                    ),
                                    @ExampleObject(
                                            name = "Formato no válido de CUIT",
                                            value = "{\"status\": \"400\", \"message\": \"Error: invalid cuit format\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Error: El CUIT '[cuit_ingresado]' no es válido o no existe en el padrón de AFIP.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "CUIT no existe en ARCA",
                                    value = "{\"status\": \"404\", \"message\": \"Error: El CUIT '[cuit_ingresado]' no es válido o no existe en el padrón de ARCA.\"}"
                            )
                    )
            )
    })
    @PostMapping("/registerCompany")
    public ResponseEntity<ResponseDTO> register(@RequestBody CompanyDTO request) throws Exception {
        companyService.createCompany(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO("201", "Company created successfully"));
    }

    // refactorizar, es solo para avanzar en el front
    @GetMapping("/getCompanyByUser/{username}")
    public ResponseEntity<?> getCompanyByUser(@PathVariable String username) {

        //verifico que esta logeado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("User is not logged in");
        }

        //obtengo el nombre

        //lo que debo hacer es que de acuerdo al nombre del usuario, verificar que exista

        String ownerUsername = authentication.getName();

        UserEntity owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!ownerUsername.equals(username)) {
            throw new ResourceNotFoundException("Los usuarios no coinciden");
        }
        return ResponseEntity.ok(companyRepository.findByUserId(owner.getId()));
    }

    /*@GetMapping("/validate/{cuit}")
    public ResponseEntity<?> validar(@PathVariable String cuit) {
        try {
            Map<String, Object> datos = afipValidationService.validateCompany(cuit);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }*/

}
