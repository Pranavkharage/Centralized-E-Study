package com.pranav.estudy.dto;

import lombok.Data;

public class StudyDTO {

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String branch;
        private String studyYear;
    }

    @Data
    public static class UpdateProfileRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String branch;
        private String studyYear;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class UserResponse {
        private Integer id;
        private String username;
        private String role;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String branch;
        private String studyYear;
        private String accessToken;
        private String tokenType = "Bearer";
    }

    @Data
    public static class ApiResponse<T> {
        private boolean success;
        private String message;
        private T data;

        public static <T> ApiResponse<T> ok(String message, T data) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = true;
            r.message = message;
            r.data = data;
            return r;
        }

        public static <T> ApiResponse<T> error(String message) {
            ApiResponse<T> r = new ApiResponse<>();
            r.success = false;
            r.message = message;
            return r;
        }
    }
}
