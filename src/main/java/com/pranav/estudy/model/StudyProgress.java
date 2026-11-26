package com.pranav.estudy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

// Which topics a student has marked as studied, per subject
@Document(collection = "study_progress")
@Data
@NoArgsConstructor
public class StudyProgress {
    @Id
    private String id;
    private String username;
    private String subject;
    private Set<String> completedTopics = new HashSet<>();
    private Instant lastStudiedAt;
}
