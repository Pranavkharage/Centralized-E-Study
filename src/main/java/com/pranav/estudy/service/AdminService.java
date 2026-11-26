package com.pranav.estudy.service;

import com.pranav.estudy.dto.AdminDTO.Stats;
import com.pranav.estudy.dto.StudyDTO.UserResponse;
import com.pranav.estudy.exception.ApiException;
import com.pranav.estudy.model.Subject;
import com.pranav.estudy.model.User;
import com.pranav.estudy.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Everything the admin panel needs. All endpoints that call this are ADMIN-only (see SecurityConfig).
@Service
public class AdminService {

    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final QuizAttemptRepository attemptRepository;
    private final QuizBankRepository bankRepository;
    private final UserService userService;

    public AdminService(UserRepository userRepository, SubjectRepository subjectRepository,
                        QuizAttemptRepository attemptRepository, QuizBankRepository bankRepository,
                        UserService userService) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.attemptRepository = attemptRepository;
        this.bankRepository = bankRepository;
        this.userService = userService;
    }

    // ---------- overview and users ----------

    public Stats stats() {
        List<Subject> subjects = subjectRepository.findAll();
        long topics = subjects.stream().mapToLong(s -> s.getTopics() == null ? 0 : s.getTopics().size()).sum();
        long users = userRepository.count();
        long admins = userRepository.countByRole("ADMIN");
        return new Stats(users, users - admins, admins, subjects.size(), topics, attemptRepository.countBySubmittedTrue());
    }

    public List<UserResponse> users() {
        return userRepository.findAll(Sort.by("id")).stream().map(userService::profileOf).toList();
    }

    public UserResponse setRole(String actingUsername, Integer id, String role) {
        if (!Set.of("STUDENT", "ADMIN").contains(role)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Role must be STUDENT or ADMIN");
        }
        User target = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        if (target.getUsername().equals(actingUsername)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot change your own role");
        }
        if ("ADMIN".equals(target.getRole()) && role.equals("STUDENT") && userRepository.countByRole("ADMIN") <= 1) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "At least one admin must remain");
        }
        target.setRole(role);
        return userService.profileOf(userRepository.save(target));
    }

    // ---------- subjects ----------

    public Subject createSubject(String rawName) {
        String name = rawName == null ? "" : rawName.trim().toUpperCase();
        if (name.length() < 2 || name.length() > 30) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Subject name must be 2 to 30 characters");
        }
        if (subjectRepository.findBySubject(name).isPresent()) {
            throw new ApiException(HttpStatus.CONFLICT, "Subject already exists");
        }
        Subject s = new Subject();
        s.setSubject(name);
        s.setTopics(new ArrayList<>());
        s.setQuestions(new ArrayList<>());
        s.setYoutubeLinks(new ArrayList<>());
        return subjectRepository.save(s);
    }

    public void deleteSubject(String name) {
        Subject s = find(name);
        bankRepository.deleteBySubject(s.getSubject());   // its generated quizzes go with it
        subjectRepository.delete(s);
    }

    // ---------- topics ----------

    public Subject addTopic(String name, Subject.Topic topic) {
        Subject s = find(name);
        List<Subject.Topic> topics = topicsOf(s);
        clean(topic);
        if (topics.stream().anyMatch(t -> t.getTopicName().equalsIgnoreCase(topic.getTopicName()))) {
            throw new ApiException(HttpStatus.CONFLICT, "A topic with this name already exists");
        }
        topics.add(topic);
        return subjectRepository.save(s);
    }

    public Subject updateTopic(String name, int index, Subject.Topic topic) {
        Subject s = find(name);
        List<Subject.Topic> topics = topicsOf(s);
        checkIndex(index, topics.size());
        clean(topic);
        for (int i = 0; i < topics.size(); i++) {
            if (i != index && topics.get(i).getTopicName().equalsIgnoreCase(topic.getTopicName())) {
                throw new ApiException(HttpStatus.CONFLICT, "A topic with this name already exists");
            }
        }
        Subject.Topic old = topics.get(index);
        if (topic.getDiagrams() == null) topic.setDiagrams(old.getDiagrams());
        bankRepository.deleteBySubjectAndTopicName(s.getSubject(), old.getTopicName()); // content changed: regenerate quiz
        topics.set(index, topic);
        return subjectRepository.save(s);
    }

    public Subject deleteTopic(String name, int index) {
        Subject s = find(name);
        List<Subject.Topic> topics = topicsOf(s);
        checkIndex(index, topics.size());
        Subject.Topic removed = topics.remove(index);
        bankRepository.deleteBySubjectAndTopicName(s.getSubject(), removed.getTopicName());
        return subjectRepository.save(s);
    }

    // ---------- interview Q&A ----------

    public Subject addQuestion(String name, Subject.QnA qa) {
        Subject s = find(name);
        if (qa.getQuestion() == null || qa.getQuestion().isBlank() || qa.getAnswer() == null || qa.getAnswer().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Question and answer are required");
        }
        qa.setQuestion(qa.getQuestion().trim());
        qa.setAnswer(qa.getAnswer().trim());
        if (s.getQuestions() == null) s.setQuestions(new ArrayList<>());
        s.getQuestions().add(qa);
        return subjectRepository.save(s);
    }

    public Subject deleteQuestion(String name, int index) {
        Subject s = find(name);
        if (s.getQuestions() == null) throw new ApiException(HttpStatus.NOT_FOUND, "Question not found");
        checkIndex(index, s.getQuestions().size());
        s.getQuestions().remove(index);
        return subjectRepository.save(s);
    }

    // ---------- helpers ----------

    private Subject find(String name) {
        return subjectRepository.findBySubject(name.trim().toUpperCase())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Subject not found"));
    }

    private List<Subject.Topic> topicsOf(Subject s) {
        if (s.getTopics() == null) s.setTopics(new ArrayList<>());
        return s.getTopics();
    }

    private void checkIndex(int index, int size) {
        if (index < 0 || index >= size) throw new ApiException(HttpStatus.NOT_FOUND, "Item not found");
    }

    private void clean(Subject.Topic t) {
        if (t.getTopicName() == null || t.getTopicName().isBlank() || t.getTopicName().length() > 120) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Topic name is required (max 120 characters)");
        }
        if (t.getTheory() == null || t.getTheory().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Theory (concept) is required");
        }
        t.setTopicName(t.getTopicName().trim());
        t.setTheory(t.getTheory().trim());
        t.setExample(t.getExample() == null ? "" : t.getExample().trim());
        t.setCode(t.getCode() == null ? "" : t.getCode());
    }
}
