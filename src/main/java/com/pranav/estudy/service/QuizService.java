package com.pranav.estudy.service;

import com.pranav.estudy.dto.QuizDTO.*;
import com.pranav.estudy.exception.ApiException;
import com.pranav.estudy.model.*;
import com.pranav.estudy.repository.QuizAttemptRepository;
import com.pranav.estudy.repository.QuizBankRepository;
import com.pranav.estudy.repository.SubjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.IntStream;

@Service
public class QuizService {

    private static final int BANK_SIZE = 10;   // questions generated once per topic

    private final SubjectRepository subjectRepository;
    private final QuizBankRepository bankRepository;
    private final QuizAttemptRepository attemptRepository;
    private final AiQuizClient aiClient;

    public QuizService(SubjectRepository subjectRepository, QuizBankRepository bankRepository,
                       QuizAttemptRepository attemptRepository, AiQuizClient aiClient) {
        this.subjectRepository = subjectRepository;
        this.bankRepository = bankRepository;
        this.attemptRepository = attemptRepository;
        this.aiClient = aiClient;
    }

    public StartResponse start(String username, StartRequest req) {
        Subject subject = findSubject(req.getSubject());
        Subject.Topic topic = findTopic(subject, req.getTopicName());

        // Reuse the stored question pool; call the AI only the first time a topic is quizzed
        QuizBank bank = bankRepository.findFirstBySubjectAndTopicName(subject.getSubject(), topic.getTopicName())
                .orElseGet(() -> createBank(subject, topic));

        int count = Math.max(3, Math.min(req.getCount() == null ? 5 : req.getCount(), 10));
        List<QuizQuestion> pool = new ArrayList<>(bank.getQuestions());
        Collections.shuffle(pool);
        List<QuizQuestion> picked = new ArrayList<>(pool.subList(0, Math.min(count, pool.size())));

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUsername(username);
        attempt.setSubject(subject.getSubject());
        attempt.setTopicName(topic.getTopicName());
        attempt.setQuestions(picked);
        attempt.setTotal(picked.size());
        attempt.setStartedAt(Instant.now());
        attempt = attemptRepository.save(attempt);

        List<PublicQuestion> visible = IntStream.range(0, picked.size())
                .mapToObj(i -> new PublicQuestion(i, picked.get(i).getQuestion(), picked.get(i).getOptions()))
                .toList();
        return new StartResponse(attempt.getId(), attempt.getSubject(), attempt.getTopicName(), visible);
    }

    public SubmitResponse submit(String username, SubmitRequest req) {
        if (req.getAttemptId() == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "attemptId is required");
        }
        // findByIdAndUsername: a student can only submit their own quiz
        QuizAttempt attempt = attemptRepository.findByIdAndUsername(req.getAttemptId(), username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Quiz not found"));
        if (attempt.isSubmitted()) {
            throw new ApiException(HttpStatus.CONFLICT, "This quiz was already submitted");
        }

        List<Integer> given = req.getAnswers() == null ? List.of() : req.getAnswers();
        List<Integer> stored = new ArrayList<>();
        List<ReviewItem> review = new ArrayList<>();
        int score = 0;

        for (int i = 0; i < attempt.getQuestions().size(); i++) {
            QuizQuestion q = attempt.getQuestions().get(i);
            Integer answer = i < given.size() ? given.get(i) : null;
            if (answer != null && (answer < 0 || answer > 3)) answer = null;
            boolean correct = answer != null && answer == q.getCorrectIndex();
            if (correct) score++;
            stored.add(answer);
            review.add(new ReviewItem(q.getQuestion(), q.getOptions(), answer, q.getCorrectIndex(), q.getExplanation(), correct));
        }

        attempt.setAnswers(stored);
        attempt.setScore(score);
        attempt.setSubmitted(true);
        attempt.setSubmittedAt(Instant.now());
        attemptRepository.save(attempt);
        return new SubmitResponse(score, attempt.getTotal(), review);
    }

    private QuizBank createBank(Subject subject, Subject.Topic topic) {
        QuizBank bank = new QuizBank();
        bank.setSubject(subject.getSubject());
        bank.setTopicName(topic.getTopicName());
        bank.setQuestions(aiClient.generate(subject.getSubject(), topic, BANK_SIZE));
        bank.setModel(aiClient.modelName());
        bank.setGeneratedAt(Instant.now());
        return bankRepository.save(bank);
    }

    Subject findSubject(String name) {
        if (name == null || name.isBlank()) throw new ApiException(HttpStatus.BAD_REQUEST, "Subject is required");
        return subjectRepository.findBySubject(name.trim().toUpperCase())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Subject not found"));
    }

    Subject.Topic findTopic(Subject subject, String topicName) {
        if (topicName == null || topicName.isBlank()) throw new ApiException(HttpStatus.BAD_REQUEST, "Topic is required");
        List<Subject.Topic> topics = subject.getTopics() == null ? List.of() : subject.getTopics();
        return topics.stream().filter(t -> topicName.equals(t.getTopicName())).findFirst()
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Topic not found"));
    }
}
