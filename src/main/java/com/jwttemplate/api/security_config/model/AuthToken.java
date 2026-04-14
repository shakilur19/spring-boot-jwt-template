package com.jwttemplate.api.security_config.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthToken {
    private String accessToken;
    private String refreshToken;
}
