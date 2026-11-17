package com.recipe.service;

import com.recipe.dto.AuthResponse;
import com.recipe.dto.LoginRequest;
import com.recipe.dto.RegisterRequest;
import com.recipe.entity.User;
import com.recipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email already exists. Please choose another email.");
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(
                request.fullName().trim(),
                request.email().trim().toLowerCase(),
                hashedPassword
        );

        User savedUser = userRepository.save(user);
        return new AuthResponse(savedUser.getId(), savedUser.getFullName(), savedUser.getEmail(), "Registration successful");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return new AuthResponse(user.getId(), user.getFullName(), user.getEmail(), "Login successful");
    }
}
