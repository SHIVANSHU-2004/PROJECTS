package com.symptomchecker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HealthReport {
    private final String patientName;
    private final String age;
    private final String symptoms;
    private final String aiAnalysis;
    private final String createdAt;

    public HealthReport(String patientName, String age, String symptoms, String aiAnalysis) {
        this.patientName = blankToDefault(patientName, "Not provided");
        this.age = blankToDefault(age, "Not provided");
        this.symptoms = symptoms.trim();
        this.aiAnalysis = aiAnalysis.trim();
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    public String toText() {
        return """
                AI MEDICAL SYMPTOM CHECKER - HEALTH REPORT
                Generated: %s

                Patient Name: %s
                Age: %s

                Symptoms Entered:
                %s

                AI / Assistant Analysis:
                %s

                Important:
                This report is for educational support only. It is not a medical diagnosis,
                prescription, or replacement for a licensed doctor. For severe symptoms,
                breathing difficulty, chest pain, fainting, stroke signs, severe dehydration,
                allergic reaction, pregnancy complications, or symptoms in infants/elderly
                patients, seek urgent medical care.
                """.formatted(createdAt, patientName, age, symptoms, aiAnalysis);
    }

    private static String blankToDefault(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }
}
