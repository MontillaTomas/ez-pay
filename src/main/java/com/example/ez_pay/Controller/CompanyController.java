package com.example.ez_pay.Controller;

import com.example.ez_pay.DTO.CompanyDTO;
import com.example.ez_pay.Service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Tag(
        name = "Controlador de la Empresa",
        description = "Endpoint para el registro de empresas"
)
public class CompanyController {
    private final CompanyService companyService;

    @Operation(
            summary = "Registrar una nueva empresa",
            description = "Crea una nueva empresa. Para ello, el usuario debe estar creado y autenticado con el ROL='Empresa'"
    )
    @PostMapping("/registerCompany")
    public ResponseEntity<String> register(@RequestBody CompanyDTO request) {
        try {
            companyService.createCompany(request);
            return ResponseEntity.ok("¡Empresa registrada exitosamente!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*
    * @PostMapping("/registerCompany")
    public ResponseEntity<ResponseDTO> register(@RequestBody CompanyDTO request) {
        try {
            companyService.createCompany(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDTO("201", "Company created successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO("500", "Internal Server Error"));
        }
    }*/
}
