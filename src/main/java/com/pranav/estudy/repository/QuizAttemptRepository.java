package com.pranav.estudy.repository;

import com.pranav.estudy.model.QuizAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuizAttemptRepository extends MongoRepository<QuizAttempt, String> {
    Optional<QuizAttempt> findByIdAndUsername(String id, String username);

    List<QuizAttempt> findByUsernameAndSubmittedTrueOrderBySubmittedAtDesc(String username);
}
