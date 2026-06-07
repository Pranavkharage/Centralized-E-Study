package com.pranav.estudy.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

// @Document tells Spring Data MongoDB: "Map this class to the 'subjects' collection"
// Replaces: db.getCollection("subjects") and manual Document field access in your old code
@Document(collection = "subjects")
@Data
@NoArgsConstructor
public class Subject {

    @Id
    private String id; // MongoDB ObjectId

    private String subject; // "OOP", "DBMS", "JAVA" etc.

    private List<Topic> topics;

    private List<String> youtubeLinks;

    private List<QnA> questions;

    // Nested classes map to embedded MongoDB documents
    // Replaces: subject.getList("topics", org.bson.Document.class)

    @Data
    @NoArgsConstructor
    public static class Topic {
        private String topicName;
        private String theory;
        private String example;
        private String code;
        private List<String> diagrams;
    }

    @Data
    @NoArgsConstructor
    public static class QnA {
        private String question;
        private String answer;
    }
}
