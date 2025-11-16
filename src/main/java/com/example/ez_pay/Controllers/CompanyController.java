package com.example.ez_pay.Controllers;

import com.example.ez_pay.DTOs.CompanyDTO;
import com.example.ez_pay.DTOs.ResponseDTO;
import com.example.ez_pay.Services.CompanyService;
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

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(
        name = "Empresa",
        description = "Endpoint para el registro de empresas"
)
public class CompanyController {
    private final CompanyService companyService;

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
    public ResponseEntity<ResponseDTO> register(@RequestBody CompanyDTO request) {
        companyService.createCompany(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDTO("201", "Company created successfully"));
    }
}
