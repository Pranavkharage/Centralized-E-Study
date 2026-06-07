package com.pranav.estudy.repository;

import com.pranav.estudy.model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// MongoRepository works exactly like JpaRepository but for MongoDB
// Replaces: collection.find(new Document("subject", subjectName)).first()
@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {

    // Spring reads method name → queries MongoDB: { "subject": subjectName }
    Optional<Subject> findBySubject(String subject);

    // Get all available subject names
    List<Subject> findAll();
}
