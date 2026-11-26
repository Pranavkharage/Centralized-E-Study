package com.pranav.estudy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

// One quiz taken by one student. Questions + correct answers stay on the server; grading is done here.
@Document(collection = "quiz_attempts")
@Data
@NoArgsConstructor
public class QuizAttempt {
    @Id
    private String id;
    private String username;
    private String subject;
    private String topicName;
    private List<QuizQuestion> questions;
    private List<Integer> answers;
    private int score;
    private int total;
    private boolean submitted;
    private Instant startedAt;
    private Instant submittedAt;
}
