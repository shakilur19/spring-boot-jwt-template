package com.jwttemplate.api.auth.controller.response;

import com.jwttemplate.api.security_config.model.AuthToken;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;

    public static LoginResponse buildLoginResponse(AuthToken token) {
        return LoginResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
