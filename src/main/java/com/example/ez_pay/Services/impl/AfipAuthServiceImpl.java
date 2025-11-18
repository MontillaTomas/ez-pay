package com.example.ez_pay.Services.impl;

import com.example.ez_pay.Services.AfipAuthService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
public class AfipAuthServiceImpl implements AfipAuthService {

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();
    private final Gson gson = new Gson();
    private String access_token = "6jmj6tpZmb6fIWrG7INzABlYoOustj4H6MREnX22iTNasTjGNqHufM88yOD5nWX1";
    private String taToken;
    private String taSign;


    @Override
    public void authenticate() {
        // Crear JSON para el request usando GSON
        try{
            Map<String, String> authRequestMap = new HashMap<>();
            authRequestMap.put("environment", "dev");
            authRequestMap.put("tax_id", "20409378472");
            authRequestMap.put("wsid", "ws_sr_constancia_inscripcion");

            String authRequestBody = gson.toJson(authRequestMap);

            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(new URI("https://app.afipsdk.com/api/v1/afip/auth"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer "+ access_token)
                    .POST(HttpRequest.BodyPublishers.ofString(authRequestBody))
                    .build();

            HttpResponse<String> authResponse = client.send(authRequest, HttpResponse.BodyHandlers.ofString());
            if (authResponse.statusCode() >= 400) {
                System.out.println("Error in auth request: " + authResponse.body());
                return;
            }

            // Parsear respuesta para extraer el sign y token usando GSON
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> authData = gson.fromJson(authResponse.body(), mapType);

            taToken = authData.get("token");
            taSign = authData.get("sign");

            System.out.println("Token: " + taToken);
            System.out.println("Sign: " + taSign);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getTaToken() { return taToken; }
    @Override
    public String getTaSign() { return taSign; }

}
