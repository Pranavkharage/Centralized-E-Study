package com.pranav.estudy.service;

import com.pranav.estudy.dto.StudyDTO.*;
import com.pranav.estudy.model.User;
import com.pranav.estudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<UserResponse> register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ApiResponse.error("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        User saved = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(saved.getId());
        response.setUsername(saved.getUsername());

        return ApiResponse.ok("Registration successful", response);
    }

    public ApiResponse<UserResponse> login(LoginRequest request) {
        // Replaces your PreparedStatement login query
        return userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setUsername(user.getUsername());
                    return ApiResponse.ok("Login successful", response);
                })
                .orElse(ApiResponse.error("Invalid username or password"));
    }
}
