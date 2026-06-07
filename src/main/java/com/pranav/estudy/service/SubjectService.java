package com.pranav.estudy.service;

import com.pranav.estudy.dto.StudyDTO.*;
import com.pranav.estudy.model.Subject;
import com.pranav.estudy.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    // Spring injects MongoRepository automatically - no MongoClient setup needed
    @Autowired
    private SubjectRepository subjectRepository;

    // Get content for a specific subject
    // Replaces: collection.find(new Document("subject", subjectName)).first()
    public ApiResponse<Subject> getSubjectContent(String subjectName) {
        return subjectRepository.findBySubject(subjectName.toUpperCase())
                .map(subject -> ApiResponse.ok("Subject found", subject))
                .orElse(ApiResponse.error("Subject '" + subjectName + "' not found in database"));
    }

    // Get list of all available subjects
    public ApiResponse<List<String>> getAllSubjects() {
        List<String> subjects = subjectRepository.findAll()
                .stream()
                .map(Subject::getSubject)
                .collect(Collectors.toList());
        return ApiResponse.ok("Available subjects", subjects);
    }

    // Add new subject content to MongoDB (admin use)
    public ApiResponse<Subject> addSubject(Subject subject) {
        // Check duplicate
        if (subjectRepository.findBySubject(subject.getSubject().toUpperCase()).isPresent()) {
            return ApiResponse.error("Subject already exists");
        }
        subject.setSubject(subject.getSubject().toUpperCase());
        Subject saved = subjectRepository.save(subject);
        return ApiResponse.ok("Subject added successfully", saved);
    }
}
