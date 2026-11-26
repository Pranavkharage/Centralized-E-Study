package com.pranav.estudy.service;

import com.pranav.estudy.dto.StudyDTO.*;
import com.pranav.estudy.model.User;
import com.pranav.estudy.repository.UserRepository;
import com.pranav.estudy.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public ApiResponse<UserResponse> register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()
                || request.getPassword() == null || request.getPassword().length() < 8) {
            return ApiResponse.error(
                    "Username is required and password must contain at least 8 characters");
        }

        String username = request.getUsername().trim();
        if (userRepository.existsByUsername(username)) {
            return ApiResponse.error("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STUDENT"); // Never accept ADMIN from public registration.

        User saved = userRepository.save(user);
        return ApiResponse.ok("Registration successful. Please log in.", toResponse(saved, false));
    }

    @Transactional
    public ApiResponse<UserResponse> login(LoginRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ApiResponse.error("Username and password are required");
        }

        return userRepository.findByUsername(request.getUsername().trim())
                .filter(user -> verifyAndUpgradeLegacyPassword(user, request.getPassword()))
                .map(user -> ApiResponse.ok("Login successful", toResponse(user, true)))
                .orElse(ApiResponse.error("Invalid username or password"));
    }

    /*
     * Existing accounts may still contain plaintext passwords from the old version.
     * On the first successful login, upgrade the value to BCrypt. New accounts are
     * BCrypt-hashed at registration. This avoids locking out existing users.
     */
    private boolean verifyAndUpgradeLegacyPassword(User user, String rawPassword) {
        String stored = user.getPassword();
        if (stored == null) return false;

        if (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$")) {
            return passwordEncoder.matches(rawPassword, stored);
        }

        if (stored.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private UserResponse toResponse(User user, boolean includeToken) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole() == null ? "STUDENT" : user.getRole());
        if (includeToken) {
            response.setAccessToken(jwtService.generateToken(user.getUsername()));
        }
        return response;
    }
}
