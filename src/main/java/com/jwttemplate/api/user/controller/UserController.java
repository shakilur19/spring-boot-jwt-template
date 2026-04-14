package com.jwttemplate.api.user.controller;

import com.jwttemplate.api.auth.controller.request.ResetPasswordRequest;
import com.jwttemplate.api.user.response.ProfileResponse;
import com.jwttemplate.api.auth.entity.User;
import com.jwttemplate.api.auth.service.UserService;
import com.jwttemplate.api.utils.common_response.CommonMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(
                ProfileResponse.buildProfile(userService.getUserProfile(user.getId()))
        );
    }


    @PostMapping("/reset-password")
    public ResponseEntity<CommonMessageResponse> resetPassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        userService.resetPassword(user.getId(), request);
        return ResponseEntity.ok(new CommonMessageResponse("Password reset successfully"));
    }
}