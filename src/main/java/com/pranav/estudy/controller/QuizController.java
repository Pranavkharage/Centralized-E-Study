package com.pranav.estudy.controller;

import com.pranav.estudy.dto.QuizDTO.*;
import com.pranav.estudy.dto.StudyDTO.ApiResponse;
import com.pranav.estudy.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // POST /api/quiz/start  { subject, topicName, count }  -> questions without answers
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<StartResponse>> start(Authentication auth, @RequestBody StartRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Quiz ready", quizService.start(auth.getName(), request)));
    }

    // POST /api/quiz/submit  { attemptId, answers:[0,2,1,...] }  -> score + review
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<SubmitResponse>> submit(Authentication auth, @RequestBody SubmitRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Quiz submitted", quizService.submit(auth.getName(), request)));
    }
}
