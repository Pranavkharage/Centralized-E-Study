package com.pranav.estudy.repository;

import com.pranav.estudy.model.StudyProgress;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StudyProgressRepository extends MongoRepository<StudyProgress, String> {
    List<StudyProgress> findByUsername(String username);

    Optional<StudyProgress> findByUsernameAndSubject(String username, String subject);
}
