package com.jwttemplate.api.user.response;

import com.jwttemplate.api.auth.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    public static ProfileResponse buildProfile(User profile) {
        return ProfileResponse.builder()
                .email(profile.getEmail())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .gender(profile.getGender())
                .build();
    }
}
