package com.example.ez_pay.Models;

public enum Category {
    AGUA("Agua"),
    ALIVIO_DE_CAJA("Alivio de Caja"),
    CEMENTERIOS_PRIVADOS("Cementerios Privados"),
    CLUBES("Clubes"),
    COMPRAS_EN_INTERNET_E_COMMERC("Compras en Internet E-Commerce"),
    COMUNICACIONES_CELULARES("Comunicaciones Celulares"),
    COMUNICACIONES_LINEA_FIJA("Comunicaciones Línea Fija"),
    COMUNICACIONES_OTROS("Comunicaciones Otros"),
    COMUNICACIONES_PREPAGOS_RECAR("Comunicaciones Prepagos Recarga"), // Expandido
    CONSEJOS_Y_COLEGIOS_PROFESIONA("Consejos y Colegios Profesionales"), // Expandido
    CONSUMO_CLIENTE_INTERMEDIO("Consumo Cliente Intermedio"),
    CONSUMO_OTROS("Consumo Otros"),
    CONSUMO_PREPAGOS("Consumo Prepagos"),
    CONSUMO_SEGURIDAD_Y_MONITOREO("Consumo Seguridad y Monitoreo"),
    CONSUMO_VENTA_DIRECTA("Consumo Venta Directa"),
    CONSUMO_VIAJES_ESTUD_TURISMO("Consumo Viajes Estudiantes/Turismo"), // Expandido
    CREDITOS_PRESTAMOS_PERSONALES("Créditos Préstamos Personales"),
    DEDICADO("Dedicado"),
    DESEMBOLSO("Desembolso"),
    ELECTRICIDAD("Electricidad"),
    ENTIDADES_DE_BIEN_PUBLICO_DON("Entidades de Bien Público Donación"), // Expandido
    EXTRACCION_DE_DINERO("Extracción de Dinero"),
    FINTECH_BANCOS_DIGITALES("Fintech Bancos Digitales"),
    GAS("Gas"),
    GESTION_DE_DEUDA("Gestión de Deuda"),
    HOME_AND_APPLIANCES("Home and Appliances"),
    INSTITUTO_DE_ENSENANZA("Instituto de Enseñanza"),
    MEDICINA_PREPAGA("Medicina Prepaga"),
    MUTUALES("Mutuales"),
    NO_DEFINIDO("No Definido"),
    OBRAS_SOCIALES("Obras Sociales"),
    PLANES_DE_VIVIENDA("Planes de Vivienda"),
    PRODUCTORES_DE_SEGUROS("Productores de Seguros"),
    SEGUROS("Seguros"),
    SINDICATOS("Sindicatos"),
    TARJETAS_DE_CREDITO("Tarjetas de Crédito"),
    TITULOS_DE_CAPITALIZACION("Títulos de Capitalización"),
    TRIBUTOS_MUNICIPALES("Tributos Municipales"),
    TRIBUTOS_NACIONALES_Y_PCIALES("Tributos Nacionales y Provinciales"),
    TV_SATELITAL_CABLE("TV Satelital/Cable");

    private final String displayName;
    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Category fromText(String text) {

        String cleanText = text.trim();

        for (Category cat : Category.values()) {
            if (cat.getDisplayName().equalsIgnoreCase(cleanText)) {
                return cat;
            }
        }
        try {
            return Category.valueOf(cleanText.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No se encontró ninguna categoría válida para el texto: '" + text + "'");
        }
    }
}
