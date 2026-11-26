package com.pranav.estudy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestion {
    private String question;
    private List<String> options;   // always 4
    private int correctIndex;       // 0-3, never sent to the browser before submit
    private String explanation;
}
