package com.pranav.estudy.controller;

import com.pranav.estudy.dto.QuizDTO.*;
import com.pranav.estudy.dto.StudyDTO.ApiResponse;
import com.pranav.estudy.service.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // POST /api/progress/topic  { subject, topicName }
    @PostMapping("/topic")
    public ResponseEntity<ApiResponse<Void>> markStudied(Authentication auth, @RequestBody TopicRequest request) {
        progressService.markTopicStudied(auth.getName(), request);
        return ResponseEntity.ok(ApiResponse.ok("Topic marked as studied", null));
    }

    // GET /api/progress/summary  -> study % per subject + quiz stats + recent attempts
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<Summary>> summary(Authentication auth) {
        return ResponseEntity.ok(ApiResponse.ok("Summary", progressService.summary(auth.getName())));
    }
}
