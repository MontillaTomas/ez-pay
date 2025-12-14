
package com.example.ez_pay.Services;

import java.net.URISyntaxException;
import java.util.Map;

public interface AfipValidationService {
    Map<String, Object> validateCompany(String cuit)  throws Exception;
}
