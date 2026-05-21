package com.symptomchecker;

public class DemoOutput {
    public static void main(String[] args) {
        String symptoms = args.length == 0
                ? "fever, cough, headache, sore throat"
                : String.join(" ", args);

        System.out.println("Sample symptoms: " + symptoms);
        System.out.println();
        System.out.println(new RuleBasedMedicalAdvisor().analyze(symptoms));
    }
}
