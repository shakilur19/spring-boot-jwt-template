package com.jwttemplate.api.auth.controller;

import com.jwttemplate.api.auth.controller.request.LoginRequest;
import com.jwttemplate.api.auth.controller.request.RefreshTokenRequest;
import com.jwttemplate.api.auth.controller.request.SignupRequest;
import com.jwttemplate.api.auth.controller.response.LoginResponse;
import com.jwttemplate.api.auth.entity.User;
import com.jwttemplate.api.auth.service.UserService;
import com.jwttemplate.api.security_config.JwtService;
import com.jwttemplate.api.security_config.model.AuthToken;
import com.jwttemplate.api.utils.common_response.CommonMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonMessageResponse> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.ok(new CommonMessageResponse("Account successfully registered"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userService.getUserByEmail(authentication.getName());
        AuthToken token = jwtService.generateToken(user);

        return ResponseEntity.ok(LoginResponse.buildLoginResponse(token));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        jwtService.validateToken(refreshToken);
        AuthToken token = jwtService.generateTokenFromClaims(jwtService.extractAllClaims(refreshToken));

        return ResponseEntity.ok(LoginResponse.buildLoginResponse(token));
    }
}