package com.symptomchecker;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SymptomCheckerFrame().setVisible(true));
    }
}
