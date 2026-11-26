package com.pranav.estudy.controller;

import com.pranav.estudy.dto.AdminDTO.Stats;
import com.pranav.estudy.dto.StudyDTO.ApiResponse;
import com.pranav.estudy.dto.StudyDTO.UserResponse;
import com.pranav.estudy.model.Subject;
import com.pranav.estudy.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Every route under /api/admin requires the ADMIN role (enforced in SecurityConfig)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService admin;

    public AdminController(AdminService admin) {
        this.admin = admin;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Stats>> stats() {
        return ResponseEntity.ok(ApiResponse.ok("Stats", admin.stats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> users() {
        return ResponseEntity.ok(ApiResponse.ok("Users", admin.users()));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserResponse>> setRole(Authentication auth, @PathVariable Integer id,
                                                             @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Role updated", admin.setRole(auth.getName(), id, body.get("role"))));
    }

    @PostMapping("/subjects")
    public ResponseEntity<ApiResponse<Subject>> createSubject(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.ok("Subject created", admin.createSubject(body.get("name"))));
    }

    @DeleteMapping("/subjects/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable String name) {
        admin.deleteSubject(name);
        return ResponseEntity.ok(ApiResponse.ok("Subject deleted", null));
    }

    @PostMapping("/subjects/{name}/topics")
    public ResponseEntity<ApiResponse<Subject>> addTopic(@PathVariable String name, @RequestBody Subject.Topic topic) {
        return ResponseEntity.ok(ApiResponse.ok("Topic added", admin.addTopic(name, topic)));
    }

    @PutMapping("/subjects/{name}/topics/{index}")
    public ResponseEntity<ApiResponse<Subject>> updateTopic(@PathVariable String name, @PathVariable int index,
                                                            @RequestBody Subject.Topic topic) {
        return ResponseEntity.ok(ApiResponse.ok("Topic updated", admin.updateTopic(name, index, topic)));
    }

    @DeleteMapping("/subjects/{name}/topics/{index}")
    public ResponseEntity<ApiResponse<Subject>> deleteTopic(@PathVariable String name, @PathVariable int index) {
        return ResponseEntity.ok(ApiResponse.ok("Topic deleted", admin.deleteTopic(name, index)));
    }

    @PostMapping("/subjects/{name}/questions")
    public ResponseEntity<ApiResponse<Subject>> addQuestion(@PathVariable String name, @RequestBody Subject.QnA qa) {
        return ResponseEntity.ok(ApiResponse.ok("Question added", admin.addQuestion(name, qa)));
    }

    @DeleteMapping("/subjects/{name}/questions/{index}")
    public ResponseEntity<ApiResponse<Subject>> deleteQuestion(@PathVariable String name, @PathVariable int index) {
        return ResponseEntity.ok(ApiResponse.ok("Question deleted", admin.deleteQuestion(name, index)));
    }
}
