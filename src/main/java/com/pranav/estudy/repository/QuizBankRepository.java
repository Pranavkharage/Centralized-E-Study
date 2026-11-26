package com.pranav.estudy.repository;

import com.pranav.estudy.model.QuizBank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuizBankRepository extends MongoRepository<QuizBank, String> {
    Optional<QuizBank> findFirstBySubjectAndTopicName(String subject, String topicName);

    long deleteBySubjectAndTopicName(String subject, String topicName);

    long deleteBySubject(String subject);
}
