package com.example.ez_pay.DTOs;

import com.example.ez_pay.Models.Category;
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
            description = "Categoría de la empresa.",
            allowableValues = {
                    "AGUA",
                    "ALIVIO_DE_CAJA",
                    "CEMENTERIOS_PRIVADOS",
                    "CLUBES",
                    "COMPRAS_EN_INTERNET_E_COMMERC",
                    "COMUNICACIONES_CELULARES",
                    "COMUNICACIONES_LINEA_FIJA",
                    "COMUNICACIONES_OTROS",
                    "COMUNICACIONES_PREPAGOS_RECAR",
                    "CONSEJOS_Y_COLEGIOS_PROFESIONA",
                    "CONSUMO_CLIENTE_INTERMEDIO",
                    "CONSUMO_OTROS",
                    "CONSUMO_PREPAGOS",
                    "CONSUMO_SEGURIDAD_Y_MONITOREO",
                    "CONSUMO_VENTA_DIRECTA",
                    "CONSUMO_VIAJES_ESTUD_TURISMO",
                    "CREDITOS_PRESTAMOS_PERSONALES",
                    "DEDICADO",
                    "DESEMBOLSO",
                    "ELECTRICIDAD",
                    "ENTIDADES_DE_BIEN_PUBLICO_DON",
                    "EXTRACCION_DE_DINERO",
                    "FINTECH_BANCOS_DIGITALES",
                    "GAS",
                    "GESTION_DE_DEUDA",
                    "HOME_AND_APPLIANCES",
                    "INSTITUTO_DE_ENSENANZA",
                    "MEDICINA_PREPAGA",
                    "MUTUALES",
                    "NO_DEFINIDO",
                    "OBRAS_SOCIALES",
                    "PLANES_DE_VIVIENDA",
                    "PRODUCTORES_DE_SEGUROS",
                    "SEGUROS",
                    "SINDICATOS",
                    "TARJETAS_DE_CREDITO",
                    "TITULOS_DE_CAPITALIZACION",
                    "TRIBUTOS_MUNICIPALES",
                    "TRIBUTOS_NACIONALES_Y_PCIALES",
                    "TV_SATELITAL_CABLE"
            }
    )
    private Category category;
    private String address;
    private String province;
    private String city;
    private int monthlyInvoices;
    private String cuit;
    private String legalName;
    private int numberOfPayments;
    private BigDecimal averageInvoice;
}
