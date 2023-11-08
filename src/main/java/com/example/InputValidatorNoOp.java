package com.example;

import jakarta.inject.Singleton;

@Singleton
public class InputValidatorNoOp implements InputValidator {
    @Override
    public String validateAccessToken(String token) {
        return "AuthorizedUser";
    }
}
