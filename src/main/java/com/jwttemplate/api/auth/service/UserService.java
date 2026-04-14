package com.jwttemplate.api.auth.service;

import com.jwttemplate.api.auth.controller.exception.UserAlreadyRegisteredException;
import com.jwttemplate.api.auth.controller.exception.UserNotFoundException;
import com.jwttemplate.api.auth.controller.request.ResetPasswordRequest;
import com.jwttemplate.api.auth.controller.request.SignupRequest;
import com.jwttemplate.api.auth.entity.CustomUserDetails;
import com.jwttemplate.api.auth.entity.User;
import com.jwttemplate.api.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    public void signup(SignupRequest request) {
        User existingUser = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail()).orElse(null);
        if (existingUser != null) {
            throw new UserAlreadyRegisteredException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCreatedAt(OffsetDateTime.now());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    public User getUserProfile(Integer id) {
        Optional<User> user = userRepository.findByIdAndDeletedAtIsNull(id);
        if(user.isPresent()) {
            return user.get();
        }else {
            throw new UserNotFoundException();
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public void resetPassword(Integer userId, ResetPasswordRequest request) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
