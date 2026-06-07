package com.pranav.estudy.controller;

import com.pranav.estudy.dto.StudyDTO.*;
import com.pranav.estudy.model.Subject;
import com.pranav.estudy.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // GET http://localhost:8080/api/subjects/all
    // Returns: list of all available subject names
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<String>>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    // GET http://localhost:8080/api/subjects/OOP
    // Returns: full content for that subject from MongoDB
    @GetMapping("/{subjectName}")
    public ResponseEntity<ApiResponse<Subject>> getSubject(@PathVariable String subjectName) {
        return ResponseEntity.ok(subjectService.getSubjectContent(subjectName));
    }

    // POST http://localhost:8080/api/subjects/add
    // Body: full Subject JSON with topics, questions, youtubeLinks
    // Use this to insert study content into MongoDB
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Subject>> addSubject(@RequestBody Subject subject) {
        return ResponseEntity.ok(subjectService.addSubject(subject));
    }
}
