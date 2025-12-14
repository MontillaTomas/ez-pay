
package com.example.ez_pay.Services;

import java.net.URISyntaxException;

public interface AfipAuthService {
    void authenticate() throws URISyntaxException;
    public String getTaToken();
    public String getTaSign();
}
