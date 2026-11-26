package com.pranav.estudy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranav.estudy.exception.ApiException;
import com.pranav.estudy.model.QuizQuestion;
import com.pranav.estudy.model.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Calls the Gemini REST API directly (no SDK, no Spring AI): one POST, JSON in, JSON out.
@Component
public class AiQuizClient {

    private static final Logger log = LoggerFactory.getLogger(AiQuizClient.class);

    private final RestClient http;
    private final ObjectMapper mapper;
    private final String apiKey;
    private final String model;

    public AiQuizClient(ObjectMapper mapper,
                        @Value("${app.ai.api-key:}") String apiKey,
                        @Value("${app.ai.model}") String model,
                        @Value("${app.ai.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);
        factory.setReadTimeout(60_000);
        this.http = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
        this.mapper = mapper;
        this.apiKey = apiKey;
        this.model = model;
    }

    public String modelName() {
        return model;
    }

    public List<QuizQuestion> generate(String subject, Subject.Topic topic, int count) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
                    "AI quiz is not configured yet. Ask the admin to set GEMINI_API_KEY.");
        }

        Map<String, Object> body = Map.of(
                "contents", List.of(Map.of("parts", List.of(Map.of("text", buildPrompt(subject, topic, count))))),
                "generationConfig", Map.of("responseMimeType", "application/json", "temperature", 0.4));

        try {
            String raw = http.post()
                    .uri("/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
            String text = mapper.readTree(raw).at("/candidates/0/content/parts/0/text").asText("");
            return parse(text);
        } catch (ApiException e) {
            throw e;
        } catch (RestClientResponseException e) {
            int status = e.getStatusCode().value();
            log.warn("AI request failed with HTTP {}", status);   // never log the API key
            if (status == 429) {
                throw new ApiException(HttpStatus.TOO_MANY_REQUESTS, "AI usage limit reached. Please try again in a minute.");
            }
            if (status == 400 || status == 403 || status == 404) {
                throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE,
                        "AI service rejected the request. The admin should check GEMINI_API_KEY and GEMINI_MODEL.");
            }
            throw new ApiException(HttpStatus.BAD_GATEWAY, "AI service is having trouble. Please try again shortly.");
        } catch (ResourceAccessException e) {
            throw new ApiException(HttpStatus.GATEWAY_TIMEOUT, "AI service timed out. Please try again.");
        } catch (Exception e) {
            log.warn("Could not read AI response: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Could not generate the quiz right now. Please try again.");
        }
    }

    private String buildPrompt(String subject, Subject.Topic topic, int count) {
        return """
                You are creating a multiple-choice quiz for a computer science student preparing for technical interviews.
                Subject: %s
                Topic: %s

                Use ONLY the study material below as the source of facts.
                --- STUDY MATERIAL ---
                Theory: %s
                Example: %s
                Code: %s
                --- END ---

                Create exactly %d questions. Rules:
                - Each question has exactly 4 options and exactly one correct option.
                - Vary the position of the correct option.
                - Mix concept questions and small practical/code questions.
                - Give a one or two sentence explanation of the correct answer.

                Return ONLY JSON in this shape:
                {"questions":[{"question":"...","options":["A","B","C","D"],"correctIndex":0,"explanation":"..."}]}
                """.formatted(subject, topic.getTopicName(),
                cut(topic.getTheory()), cut(topic.getExample()), cut(topic.getCode()), count);
    }

    private String cut(String s) {
        if (s == null) return "";
        return s.length() > 3000 ? s.substring(0, 3000) : s;
    }

    private List<QuizQuestion> parse(String text) throws Exception {
        String t = text.trim();
        if (t.startsWith("```")) {
            t = t.replaceAll("^```(?:json)?\\s*", "").replaceAll("\\s*```$", "");
        }
        JsonNode root = mapper.readTree(t);
        JsonNode arr = root.isArray() ? root : root.path("questions");

        List<QuizQuestion> out = new ArrayList<>();
        for (JsonNode n : arr) {
            JsonNode opts = n.path("options");
            int correct = n.path("correctIndex").asInt(-1);
            String q = n.path("question").asText("").trim();
            if (q.isEmpty() || !opts.isArray() || opts.size() != 4 || correct < 0 || correct > 3) continue; // skip malformed
            List<String> options = new ArrayList<>();
            opts.forEach(o -> options.add(o.asText("").trim()));
            if (options.stream().anyMatch(String::isEmpty)) continue;
            out.add(new QuizQuestion(q, options, correct, n.path("explanation").asText("").trim()));
        }
        if (out.size() < 3) {
            throw new IllegalStateException("AI returned too few valid questions");
        }
        return out;
    }
}
