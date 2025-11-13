package com.example.ez_pay;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "EZPAY - REST API",
                description = "Esta API documenta todos los servicios y endpoints para la plataforma EZPAY.<br>" +
                        "Permite gestionar la **autenticación** de usuarios (JWT), el registro de **Empleados** y **Empresas**, " +
                        "y las operaciones principales del sistema.",
                version = "v1.0.0",
                contact = @Contact(
                        name = "Milagros Sachetti",
                        email = "milagrossachetti@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Repositorio del Proyecto en GitHub",
                url = "https://github.com/MontillaTomas/ez-pay"
        )
)
public class EzPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzPayApplication.class, args);
	}

}
