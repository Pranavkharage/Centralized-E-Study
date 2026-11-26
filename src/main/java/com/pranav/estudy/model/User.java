package com.pranav.estudy.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// This maps to MySQL - handles login/register (same as your authentication.sql)
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    // Public registration always assigns STUDENT. Promote an account to ADMIN
    // manually using the supplied SQL migration after verifying the account.
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'STUDENT'")
    private String role = "STUDENT";
}
