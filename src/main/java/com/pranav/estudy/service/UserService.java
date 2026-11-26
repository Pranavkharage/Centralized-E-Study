package com.pranav.estudy.service;

import com.pranav.estudy.dto.StudyDTO.*;
import com.pranav.estudy.model.User;
import com.pranav.estudy.repository.UserRepository;
import com.pranav.estudy.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Pattern NAME = Pattern.compile("^[\\p{L} .'-]{1,50}$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^\\+?[0-9]{10,13}$");
    private static final Set<String> YEARS = Set.of("First Year", "Second Year", "Third Year", "Final Year", "Graduated");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public ApiResponse<UserResponse> register(RegisterRequest request) {
        String username = clean(request.getUsername());
        if (username.length() < 3 || username.length() > 50) {
            return ApiResponse.error("Username must be 3 to 50 characters");
        }
        if (request.getPassword() == null || request.getPassword().length() < 8) {
            return ApiResponse.error("Password must contain at least 8 characters");
        }
        String problem = validateDetails(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPhone(), request.getBranch(), request.getStudyYear(), true);
        if (problem != null) return ApiResponse.error(problem);

        String email = clean(request.getEmail()).toLowerCase();
        if (userRepository.existsByUsername(username)) return ApiResponse.error("Username already exists");
        if (userRepository.existsByEmail(email)) return ApiResponse.error("Email already registered");

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STUDENT"); // Never accept ADMIN from public registration.
        user.setFirstName(clean(request.getFirstName()));
        user.setLastName(clean(request.getLastName()));
        user.setEmail(email);
        user.setPhone(normalizePhone(request.getPhone()));
        user.setBranch(clean(request.getBranch()));
        user.setStudyYear(clean(request.getStudyYear()));
        return ApiResponse.ok("Registration successful. Please log in.", toResponse(userRepository.save(user), false));
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

    public ApiResponse<UserResponse> getProfile(String username) {
        return userRepository.findByUsername(username)
                .map(u -> ApiResponse.ok("Profile", toResponse(u, false)))
                .orElse(ApiResponse.error("User not found"));
    }

    @Transactional
    public ApiResponse<UserResponse> updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return ApiResponse.error("User not found");

        String problem = validateDetails(request.getFirstName(), request.getLastName(), request.getEmail(),
                request.getPhone(), request.getBranch(), request.getStudyYear(), false);
        if (problem != null) return ApiResponse.error(problem);

        String email = clean(request.getEmail()).toLowerCase();
        String current = user.getEmail() == null ? "" : user.getEmail();
        if (!email.isEmpty() && !email.equals(current) && userRepository.existsByEmail(email)) {
            return ApiResponse.error("Email already registered");
        }
        user.setFirstName(clean(request.getFirstName()));
        user.setLastName(clean(request.getLastName()));
        user.setEmail(email.isEmpty() ? null : email);
        user.setPhone(clean(request.getPhone()).isEmpty() ? null : normalizePhone(request.getPhone()));
        user.setBranch(clean(request.getBranch()).isEmpty() ? null : clean(request.getBranch()));
        user.setStudyYear(clean(request.getStudyYear()).isEmpty() ? null : clean(request.getStudyYear()));
        return ApiResponse.ok("Profile updated", toResponse(userRepository.save(user), false));
    }

    // Public so the admin service can list users in the same safe shape (never includes the password)
    public UserResponse profileOf(User user) {
        return toResponse(user, false);
    }

    // strict = registration (everything required); otherwise only names are required and the rest is checked if given
    private String validateDetails(String first, String last, String email, String phone,
                                   String branch, String year, boolean strict) {
        if (!NAME.matcher(clean(first)).matches()) return "Enter a valid first name";
        if (!NAME.matcher(clean(last)).matches()) return "Enter a valid last name";
        if ((strict || !clean(email).isEmpty()) && !EMAIL.matcher(clean(email)).matches()) {
            return "Enter a valid email address";
        }
        if ((strict || !clean(phone).isEmpty()) && !PHONE.matcher(normalizePhone(phone)).matches()) {
            return "Enter a valid phone number (10 to 13 digits)";
        }
        if ((strict || !clean(branch).isEmpty()) && (clean(branch).isEmpty() || clean(branch).length() > 60)) {
            return "Select your branch";
        }
        if ((strict || !clean(year).isEmpty()) && !YEARS.contains(clean(year))) {
            return "Select your year of study";
        }
        return null;
    }

    /*
     * Existing accounts may still contain plaintext passwords from the old version.
     * On the first successful login, upgrade the value to BCrypt.
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
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setUsername(user.getUsername());
        r.setRole(user.getRole() == null ? "STUDENT" : user.getRole());
        r.setFirstName(user.getFirstName());
        r.setLastName(user.getLastName());
        r.setEmail(user.getEmail());
        r.setPhone(user.getPhone());
        r.setBranch(user.getBranch());
        r.setStudyYear(user.getStudyYear());
        if (includeToken) {
            r.setAccessToken(jwtService.generateToken(user.getUsername()));
        }
        return r;
    }

    private static String clean(String s) {
        return s == null ? "" : s.trim();
    }

    private static String normalizePhone(String s) {
        return clean(s).replaceAll("[\\s-]", "");
    }
}
