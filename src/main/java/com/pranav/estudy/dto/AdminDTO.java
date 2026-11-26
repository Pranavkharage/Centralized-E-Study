package com.pranav.estudy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

public class AdminDTO {

    @Data
    @AllArgsConstructor
    public static class Stats {
        private long users;
        private long students;
        private long admins;
        private long subjects;
        private long topics;
        private long quizzesTaken;
    }
}
