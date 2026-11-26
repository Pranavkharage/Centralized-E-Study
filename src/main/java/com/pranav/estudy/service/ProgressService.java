package com.pranav.estudy.service;

import com.pranav.estudy.dto.QuizDTO.*;
import com.pranav.estudy.model.QuizAttempt;
import com.pranav.estudy.model.StudyProgress;
import com.pranav.estudy.model.Subject;
import com.pranav.estudy.repository.QuizAttemptRepository;
import com.pranav.estudy.repository.StudyProgressRepository;
import com.pranav.estudy.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    private final SubjectRepository subjectRepository;
    private final StudyProgressRepository progressRepository;
    private final QuizAttemptRepository attemptRepository;
    private final QuizService quizService;

    public ProgressService(SubjectRepository subjectRepository, StudyProgressRepository progressRepository,
                           QuizAttemptRepository attemptRepository, QuizService quizService) {
        this.subjectRepository = subjectRepository;
        this.progressRepository = progressRepository;
        this.attemptRepository = attemptRepository;
        this.quizService = quizService;
    }

    public void markTopicStudied(String username, TopicRequest req) {
        Subject subject = quizService.findSubject(req.getSubject());
        Subject.Topic topic = quizService.findTopic(subject, req.getTopicName());   // must be a real topic

        StudyProgress progress = progressRepository.findByUsernameAndSubject(username, subject.getSubject())
                .orElseGet(() -> {
                    StudyProgress p = new StudyProgress();
                    p.setUsername(username);
                    p.setSubject(subject.getSubject());
                    return p;
                });
        progress.getCompletedTopics().add(topic.getTopicName());
        progress.setLastStudiedAt(Instant.now());
        progressRepository.save(progress);
    }

    public Summary summary(String username) {
        Map<String, StudyProgress> bySubject = progressRepository.findByUsername(username).stream()
                .collect(Collectors.toMap(StudyProgress::getSubject, p -> p, (a, b) -> a));

        List<SubjectProgress> subjects = new ArrayList<>();
        int totalTopics = 0, completedTopics = 0;
        for (Subject s : subjectRepository.findAll()) {
            List<String> names = s.getTopics() == null ? List.of()
                    : s.getTopics().stream().map(Subject.Topic::getTopicName).toList();
            StudyProgress p = bySubject.get(s.getSubject());
            // count only topics that still exist in the subject
            List<String> done = p == null ? List.of() : names.stream().filter(p.getCompletedTopics()::contains).toList();
            subjects.add(new SubjectProgress(s.getSubject(), names.size(), done.size(), done));
            totalTopics += names.size();
            completedTopics += done.size();
        }

        List<QuizAttempt> attempts = attemptRepository.findByUsernameAndSubmittedTrueOrderBySubmittedAtDesc(username);
        int sum = 0, best = 0;
        for (QuizAttempt a : attempts) {
            int pct = a.getTotal() == 0 ? 0 : Math.round(100f * a.getScore() / a.getTotal());
            sum += pct;
            best = Math.max(best, pct);
        }
        int average = attempts.isEmpty() ? 0 : Math.round((float) sum / attempts.size());
        int percent = totalTopics == 0 ? 0 : Math.round(100f * completedTopics / totalTopics);

        List<AttemptSummary> recent = attempts.stream().limit(10)
                .map(a -> new AttemptSummary(a.getId(), a.getSubject(), a.getTopicName(),
                        a.getScore(), a.getTotal(), a.getSubmittedAt()))
                .toList();

        return new Summary(totalTopics, completedTopics, percent, attempts.size(), average, best, subjects, recent);
    }
}
