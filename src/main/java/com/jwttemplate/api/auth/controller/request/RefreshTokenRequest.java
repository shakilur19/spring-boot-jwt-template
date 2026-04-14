package com.jwttemplate.api.auth.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotNull
    private String refreshToken;
}
