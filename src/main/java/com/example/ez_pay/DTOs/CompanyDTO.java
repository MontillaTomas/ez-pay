package com.example.ez_pay.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(
        name = "CompanyDTO",
        description = "DTO que contiene los datos necesarios para registrar una Empresa. Para poder hacerlo, es necesario que anteriormente se haya registrado en la plataforma con el ROL='Empresa' "
)
public class CompanyDTO {
    @Schema(
            description = "Categoría de la empresa",
            allowableValues = {
                    "Agua",
                    "Alivio de Caja",
                    "Cementerios Privados",
                    "Clubes",
                    "Compras en Internet E-Commerce",
                    "Comunicaciones Celulares",
                    "Comunicaciones Línea Fija",
                    "Comunicaciones Otros",
                    "Comunicaciones Prepagos Recarga",
                    "Consejos y Colegios Profesionales",
                    "Consumo Cliente Intermedio",
                    "Consumo Otros",
                    "Consumo Prepagos",
                    "Consumo Seguridad y Monitoreo",
                    "Consumo Venta Directa",
                    "Consumo Viajes Estudiantes/Turismo",
                    "Créditos Préstamos Personales",
                    "Dedicado",
                    "Desembolso",
                    "Electricidad",
                    "Entidades de Bien Público Donación",
                    "Extracción de Dinero",
                    "Fintech Bancos Digitales",
                    "Gas",
                    "Gestión de Deuda",
                    "Home and Appliances",
                    "Instituto de Enseñanza",
                    "Medicina Prepaga",
                    "Mutuales",
                    "No Definido",
                    "Obras Sociales",
                    "Planes de Vivienda",
                    "Productores de Seguros",
                    "Seguros",
                    "Sindicatos",
                    "Tarjetas de Crédito",
                    "Títulos de Capitalización",
                    "Tributos Municipales",
                    "Tributos Nacionales y Provinciales",
                    "TV Satelital/Cable"
            }
    )
    private String category;
    @Schema(
            description = "Dirección de la empresa",
            example = "1122334455"
    )
    private String address;
    @Schema(
            description = "Provincia donde se localiza la empresa.",
            example = "Tucumán"
    )
    private String province;

    @Schema(
            description = "Ciudad donde se localiza la empresa.",
            example = "San Miguel de Tucumán"
    )
    private String city;

    @Schema(
            description = "Cantidad de facturas que la empresa emite mensualmente (promedio).",
            example = "250"
    )
    private int monthlyInvoices;

    @Schema(
            description = "Clave Única de Identificación Tributaria (CUIT) de la empresa.",
            example = "33693450239",
            allowableValues = {"33693450239", "30558515305", "30202020204"
            }
    )
    private String cuit;

    @Schema(
            description = "Razón social de la empresa (nombre legal).",
            example = "Servicios Integrales S.R.L."
    )
    private String legalName;

    @Schema(
            description = "Número de pagos que la empresa procesa o recibe mensualmente (promedio).",
            example = "400"
    )
    private int numberOfPayments;

    @Schema(
            description = "Monto promedio (en pesos) de las facturas de la empresa.",
            example = "15000.75"
    )
    private BigDecimal averageInvoice;
}
