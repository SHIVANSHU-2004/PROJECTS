package com.symptomchecker;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SymptomCheckerFrame extends JFrame {
    private final JTextField nameField = new JTextField();
    private final JTextField ageField = new JTextField();
    private final JTextArea symptomsArea = new JTextArea();
    private final JTextArea resultArea = new JTextArea();
    private final JButton analyzeButton = new JButton("Analyze Symptoms");
    private final JButton saveReportButton = new JButton("Save Health Report");
    private final AiMedicalClient aiClient = new AiMedicalClient();

    public SymptomCheckerFrame() {
        super("AI Medical Symptom Checker");
        configureLookAndFeel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 680));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(headerPanel(), BorderLayout.NORTH);
        add(formPanel(), BorderLayout.CENTER);
        add(buttonPanel(), BorderLayout.SOUTH);

        analyzeButton.addActionListener(event -> analyzeSymptoms());
        saveReportButton.addActionListener(event -> saveReport());
    }

    private JPanel headerPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(21, 93, 110));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("AI Medical Symptom Checker");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Enter symptoms, review possible conditions, medicine guidance, and generate a health report.");
        subtitle.setForeground(new Color(219, 245, 247));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panel.add(title, BorderLayout.NORTH);
        panel.add(subtitle, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel formPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        panel.setBackground(new Color(247, 250, 252));

        symptomsArea.setLineWrap(true);
        symptomsArea.setWrapStyleWord(true);
        symptomsArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        symptomsArea.setRows(8);

        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        resultArea.setText("Your AI health report will appear here.");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(label("Patient Name"), c);
        c.gridx = 1;
        panel.add(label("Age"), c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(nameField, c);
        c.gridx = 1;
        panel.add(ageField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        panel.add(label("Symptoms"), c);

        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.35;
        panel.add(new JScrollPane(symptomsArea), c);

        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        panel.add(label("AI Result / Health Report Preview"), c);

        c.gridy = 5;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.65;
        panel.add(new JScrollPane(resultArea), c);

        return panel;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        panel.add(analyzeButton);
        panel.add(saveReportButton);
        return panel;
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private void analyzeSymptoms() {
        String symptoms = symptomsArea.getText();
        if (symptoms == null || symptoms.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter symptoms first.", "Symptoms required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        analyzeButton.setEnabled(false);
        resultArea.setText("Analyzing symptoms. Please wait...");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return aiClient.analyze(nameField.getText(), ageField.getText(), symptoms);
            }

            @Override
            protected void done() {
                analyzeButton.setEnabled(true);
                try {
                    resultArea.setText(get());
                    resultArea.setCaretPosition(0);
                } catch (Exception ex) {
                    resultArea.setText("Could not analyze symptoms.\n\n" + ex.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void saveReport() {
        if (symptomsArea.getText().isBlank() || resultArea.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Analyze symptoms before saving a report.", "No report yet", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("health-report.txt"));
        int choice = chooser.showSaveDialog(this);
        if (choice != JFileChooser.APPROVE_OPTION) {
            return;
        }

        HealthReport report = new HealthReport(nameField.getText(), ageField.getText(), symptomsArea.getText(), resultArea.getText());
        try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
            writer.write(report.toText());
            JOptionPane.showMessageDialog(this, "Health report saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save report: " + ex.getMessage(), "Save failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void configureLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Swing's default look and feel is acceptable if the system theme is unavailable.
        }
    }
}
