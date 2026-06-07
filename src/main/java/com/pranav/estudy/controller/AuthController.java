package com.pranav.estudy.controller;

import com.pranav.estudy.dto.StudyDTO.*;
import com.pranav.estudy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // POST http://localhost:8080/api/auth/register
    // Body: { "username": "pranav", "password": "1234" }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // POST http://localhost:8080/api/auth/login
    // Body: { "username": "pranav", "password": "1234" }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
