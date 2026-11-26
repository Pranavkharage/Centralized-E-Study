package com.pranav.estudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

public class QuizDTO {

    @Data
    public static class StartRequest {
        private String subject;
        private String topicName;
        private Integer count;
    }

    // A question as the student sees it: no correct answer
    @Data
    @AllArgsConstructor
    public static class PublicQuestion {
        private int index;
        private String question;
        private List<String> options;
    }

    @Data
    @AllArgsConstructor
    public static class StartResponse {
        private String attemptId;
        private String subject;
        private String topicName;
        private List<PublicQuestion> questions;
    }

    @Data
    public static class SubmitRequest {
        private String attemptId;
        private List<Integer> answers;   // chosen option index per question, null = unanswered
    }

    @Data
    @AllArgsConstructor
    public static class ReviewItem {
        private String question;
        private List<String> options;
        private Integer yourAnswer;
        private int correctIndex;
        private String explanation;
        private boolean correct;
    }

    @Data
    @AllArgsConstructor
    public static class SubmitResponse {
        private int score;
        private int total;
        private List<ReviewItem> review;
    }

    @Data
    public static class TopicRequest {
        private String subject;
        private String topicName;
    }

    @Data
    @AllArgsConstructor
    public static class AttemptSummary {
        private String id;
        private String subject;
        private String topicName;
        private int score;
        private int total;
        private Instant submittedAt;
    }

    @Data
    @AllArgsConstructor
    public static class SubjectProgress {
        private String subject;
        private int totalTopics;
        private int completedTopics;
        private List<String> completed;
    }

    @Data
    @AllArgsConstructor
    public static class Summary {
        private int totalTopics;
        private int completedTopics;
        private int percent;
        private int attempts;
        private int averagePercent;
        private int bestPercent;
        private List<SubjectProgress> subjects;
        private List<AttemptSummary> recentAttempts;
    }
}
