package com.symptomchecker;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AiMedicalClient {
    private static final String DEFAULT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String DEFAULT_MODEL = "gpt-4o-mini";

    private final RuleBasedMedicalAdvisor fallbackAdvisor = new RuleBasedMedicalAdvisor();

    public String analyze(String patientName, String age, String symptoms) throws IOException, InterruptedException {
        String apiKey = System.getenv("AI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return "Local mode: AI_API_KEY is not set, so this result uses the built-in medical guidance engine.\n\n"
                    + fallbackAdvisor.analyze(symptoms);
        }

        String apiUrl = envOrDefault("AI_API_URL", DEFAULT_API_URL);
        String model = envOrDefault("AI_MODEL", DEFAULT_MODEL);

        String systemPrompt = """
                You are a cautious medical symptom checker for educational support.
                Do not claim to diagnose. Suggest possible conditions, safe self-care,
                over-the-counter medicine categories only when appropriate, and red flags.
                Do not provide exact dosages. Always advise consulting a licensed doctor.
                Return a concise report with headings:
                Possible conditions, Medicine / care suggestions, Health report, Urgent care warning.
                """;

        String userPrompt = """
                Patient name: %s
                Age: %s
                Symptoms: %s
                """.formatted(blank(patientName), blank(age), symptoms.trim());

        String requestBody = """
                {
                  "model": "%s",
                  "messages": [
                    {"role": "system", "content": "%s"},
                    {"role": "user", "content": "%s"}
                  ],
                  "temperature": 0.2
                }
                """.formatted(jsonEscape(model), jsonEscape(systemPrompt), jsonEscape(userPrompt));

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .timeout(Duration.ofSeconds(45))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            return """
                    AI API request failed with status %d.
                    Response:
                    %s

                    Local fallback result:
                    %s
                    """.formatted(response.statusCode(), response.body(), fallbackAdvisor.analyze(symptoms));
        }

        String content = extractMessageContent(response.body());
        if (content.isBlank()) {
            return "AI API returned an unexpected response.\n\nLocal fallback result:\n" + fallbackAdvisor.analyze(symptoms);
        }
        return content;
    }

    private static String extractMessageContent(String json) {
        int keyIndex = json.indexOf("\"content\"");
        if (keyIndex < 0) {
            return "";
        }
        int colonIndex = json.indexOf(':', keyIndex);
        if (colonIndex < 0) {
            return "";
        }
        int startQuote = json.indexOf('"', colonIndex + 1);
        if (startQuote < 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        boolean escaping = false;
        for (int i = startQuote + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaping) {
                result.append(switch (c) {
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case '"', '\\', '/' -> c;
                    default -> c;
                });
                escaping = false;
            } else if (c == '\\') {
                escaping = true;
            } else if (c == '"') {
                break;
            } else {
                result.append(c);
            }
        }
        return result.toString().trim();
    }

    private static String jsonEscape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String envOrDefault(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private static String blank(String value) {
        return value == null || value.isBlank() ? "Not provided" : value.trim();
    }
}
