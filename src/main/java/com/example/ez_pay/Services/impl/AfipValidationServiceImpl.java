package com.example.ez_pay.Services.impl;

import com.example.ez_pay.Exceptions.ResourceNotFoundException;
import com.example.ez_pay.Services.AfipAuthService;
import com.example.ez_pay.Services.AfipValidationService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AfipValidationServiceImpl implements AfipValidationService {
    @Autowired
    private final AfipAuthService afipAuthService;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();
    private final Gson gson = new Gson();


    public AfipValidationServiceImpl(AfipAuthService afipAuthService) {
        this.afipAuthService = afipAuthService;
    }


    @Override
    public Map<String, Object> validateCompany(String cuit) throws Exception {
        afipAuthService.authenticate();

        Map<String, Object> inscriptionRequestMap = new LinkedHashMap<>();
        inscriptionRequestMap.put("environment", "dev");
        inscriptionRequestMap.put("method", "getPersona_v2");
        inscriptionRequestMap.put("wsid", "ws_sr_constancia_inscripcion");

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("token", afipAuthService.getTaToken());
        params.put("sign", afipAuthService.getTaSign());
        params.put("cuitRepresentada", "20409378472");
        params.put("idPersona", cuit);  //no sé si aceptan string, veremos

        inscriptionRequestMap.put("params", params);

        String inscriptionRequestBody = gson.toJson(inscriptionRequestMap);

        HttpRequest inscriptionRequest = HttpRequest.newBuilder()
                .uri(new URI("https://app.afipsdk.com/api/v1/afip/requests"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 6jmj6tpZmb6fIWrG7INzABlYoOustj4H6MREnX22iTNasTjGNqHufM88yOD5nWX1")
                .POST(HttpRequest.BodyPublishers.ofString(inscriptionRequestBody))
                .build();

        HttpResponse<String> inscriptionResponse = httpClient.send(inscriptionRequest, HttpResponse.BodyHandlers.ofString());

        if (inscriptionResponse.statusCode() >= 400) {
            throw new ResourceNotFoundException("Error al consultar constancia: " + inscriptionResponse.body());
        }

        System.out.println("Response: " + inscriptionResponse.body());

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Object> resp = gson.fromJson(inscriptionResponse.body(), type);
        return resp;

    }
}
