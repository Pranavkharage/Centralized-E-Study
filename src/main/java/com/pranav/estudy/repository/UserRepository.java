package com.pranav.estudy.repository;

import com.pranav.estudy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // SELECT * FROM users WHERE username = ? AND password = ?
    Optional<User> findByUsernameAndPassword(String username, String password);

    boolean existsByUsername(String username);
}
