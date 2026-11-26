package com.pranav.estudy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

// AI-generated question pool for ONE topic. Generated once, reused by every student (saves AI quota).
@Document(collection = "quiz_banks")
@Data
@NoArgsConstructor
public class QuizBank {
    @Id
    private String id;
    private String subject;
    private String topicName;
    private List<QuizQuestion> questions;
    private String model;
    private Instant generatedAt;
}
