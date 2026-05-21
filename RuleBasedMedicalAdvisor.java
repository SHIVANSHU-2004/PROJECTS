package com.symptomchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RuleBasedMedicalAdvisor {
    public String analyze(String symptoms) {
        String text = symptoms.toLowerCase(Locale.ROOT);
        List<String> possibleConditions = new ArrayList<>();
        List<String> selfCare = new ArrayList<>();
        List<String> redFlags = new ArrayList<>();

        if (containsAny(text, "fever", "temperature", "chills")) {
            possibleConditions.add("Viral infection, flu, COVID-like illness, malaria/dengue risk depending on location and exposure");
            selfCare.add("Rest, drink fluids, monitor temperature, and consider paracetamol/acetaminophen as per the medicine label if safe for you");
            redFlags.add("Fever above 103 F / 39.4 C, fever lasting more than 3 days, stiff neck, confusion, rash, or severe weakness");
        }
        if (containsAny(text, "cough", "cold", "sore throat", "runny nose", "congestion")) {
            possibleConditions.add("Common cold, throat infection, allergies, bronchitis, or flu-like illness");
            selfCare.add("Warm fluids, steam inhalation, saline gargle/spray, honey for cough if age is above 1 year");
            redFlags.add("Shortness of breath, blue lips, chest pain, blood in cough, or oxygen problems");
        }
        if (containsAny(text, "headache", "migraine")) {
            possibleConditions.add("Tension headache, migraine, dehydration, sinus infection, eye strain, or high blood pressure");
            selfCare.add("Hydration, rest in a quiet room, reduce screen brightness, and use common pain relief only if safe for you");
            redFlags.add("Sudden worst headache, head injury, weakness on one side, vision loss, fainting, or high fever with neck stiffness");
        }
        if (containsAny(text, "stomach", "vomit", "nausea", "diarrhea", "loose motion", "abdominal")) {
            possibleConditions.add("Indigestion, food poisoning, viral gastroenteritis, acidity, or dehydration");
            selfCare.add("Oral rehydration solution, bland food, small frequent sips of water, and avoid oily/spicy foods for now");
            redFlags.add("Blood in stool/vomit, severe abdominal pain, persistent vomiting, signs of dehydration, or symptoms in pregnancy");
        }
        if (containsAny(text, "chest pain", "breathless", "shortness of breath", "difficulty breathing")) {
            possibleConditions.add("Could include asthma, pneumonia, anxiety, heart-related causes, or other urgent conditions");
            redFlags.add("Chest pain or breathing difficulty can be urgent. Seek emergency medical care immediately.");
        }
        if (containsAny(text, "rash", "itch", "swelling", "allergy")) {
            possibleConditions.add("Allergic reaction, skin infection, eczema, hives, or medicine/food reaction");
            selfCare.add("Avoid suspected triggers, keep the area clean, and ask a pharmacist/doctor before using antihistamines or creams");
            redFlags.add("Face/lip/tongue swelling, breathing difficulty, rapidly spreading rash, fever with rash, or severe pain");
        }

        if (possibleConditions.isEmpty()) {
            possibleConditions.add("The symptoms are not specific enough for a reliable suggestion. More details such as duration, severity, temperature, medicines taken, and medical history are needed.");
            selfCare.add("Track symptoms, hydrate, rest, and consult a doctor if symptoms continue, worsen, or feel unusual.");
            redFlags.add("Seek urgent care for severe pain, breathing trouble, chest pain, fainting, confusion, uncontrolled bleeding, or sudden weakness.");
        }

        return format(possibleConditions, selfCare, redFlags);
    }

    private String format(List<String> possibleConditions, List<String> selfCare, List<String> redFlags) {
        return """
                Possible conditions to discuss with a doctor:
                %s

                Medicine / care suggestions:
                %s

                When to seek urgent care:
                %s

                Note: Do not start antibiotics, steroids, prescription medicines, or high-dose painkillers without a clinician. Medicine safety depends on age, pregnancy, allergies, kidney/liver disease, other illnesses, and current medicines.
                """.formatted(bullets(possibleConditions), bullets(selfCare), bullets(redFlags));
    }

    private static String bullets(List<String> values) {
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            builder.append("- ").append(value).append(System.lineSeparator());
        }
        return builder.toString().trim();
    }

    private static boolean containsAny(String text, String... needles) {
        for (String needle : needles) {
            if (text.contains(needle)) {
                return true;
            }
        }
        return false;
    }
}
