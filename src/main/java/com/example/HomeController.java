package com.example;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;

import java.util.Collections;
import java.util.Map;

@Controller
public class HomeController {

    private final InputValidator inputValidator;

    public HomeController(InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    @Get
    public Map<String, Object> index() {
        return Collections.singletonMap("message", "Hello World");
    }

    @Get("/validate/token")
    public HttpResponse<String> validateToken(@Header("accessToken") String oAuthToken) {
        String userId = inputValidator.validateAccessToken(oAuthToken);
        return HttpResponse.ok(String.format("User '%s' VALIDATED", userId));
    }
}
