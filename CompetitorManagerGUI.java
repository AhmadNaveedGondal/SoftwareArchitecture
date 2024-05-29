import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompetitorManagerGUI extends JFrame {
    private final CompetitorList competitorList;
    private static final String COMPETITOR_FILE = "RunCompetitor.csv";
    private static final String REPORT_FILE = "TheFinalReport.txt";

    private JTextField compNumField;
    private JTextField compNameField;
    private JTextField compEmailField;
    private JTextField compDOBField;
    private JTextField compCategoryField;
    private JTextField compLevelField;
    private JTextArea outputArea;

    public CompetitorManagerGUI() {
        competitorList = new CompetitorList();

        outputArea = new JTextArea();
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        loadFileData();

        setTitle("Competition Manager");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 3));

        inputPanel.add(new JLabel("Competitor Number:"));
        compNumField = new JTextField();
        inputPanel.add(compNumField);

        inputPanel.add(new JLabel("Name:"));
        compNameField = new JTextField();
        inputPanel.add(compNameField);

        inputPanel.add(new JLabel("Email:"));
        compEmailField = new JTextField();
        inputPanel.add(compEmailField);

        inputPanel.add(new JLabel("Date of Birth (dd/MM/yyyy):"));
        compDOBField = new JTextField();
        inputPanel.add(compDOBField);

        inputPanel.add(new JLabel("Category:"));
        compCategoryField = new JTextField();
        inputPanel.add(compCategoryField);

        inputPanel.add(new JLabel("Level:"));
        compLevelField = new JTextField();
        inputPanel.add(compLevelField);

        JButton registerButton = new JButton("Register Competitor");
        registerButton.addActionListener(new RegisterButtonListener());
        
        inputPanel.add(registerButton);

        JButton updateButton = new JButton("Update Competitor");
        updateButton.addActionListener(new UpdateButtonListener());
        inputPanel.add(updateButton);

        JButton removeButton = new JButton("Remove Competitor");
        removeButton.addActionListener(new RemoveButtonListener());
        inputPanel.add(removeButton);

        JButton scoreButton = new JButton("Give Scores");
        scoreButton.addActionListener(new ScoreButtonListener());
        inputPanel.add(scoreButton);

        JButton viewAllButton = new JButton("View All Competitors");
        viewAllButton.addActionListener(new ViewAllButtonListener());
        
        inputPanel.add(viewAllButton);

        JButton fullDetailsButton = new JButton("Full Details");
        fullDetailsButton.addActionListener(new ViewDetailsButtonListener("FULL"));
        inputPanel.add(fullDetailsButton);

        JButton shortDetailsButton = new JButton("Short Details");
        shortDetailsButton.addActionListener(new ViewDetailsButtonListener("SHORT"));
        inputPanel.add(shortDetailsButton);

        JButton reportButton = new JButton("Show Report");
        reportButton.addActionListener(new ShowReportButtonListener());
        inputPanel.add(reportButton);

        JButton saveButton = new JButton("Save Data");
        saveButton.addActionListener(new SaveDataButtonListener());
        inputPanel.add(saveButton);

        JButton saveReportButton = new JButton("Save Report");
        saveReportButton.addActionListener(new SaveReportButtonListener());
        inputPanel.add(saveReportButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ExitButtonListener());
        inputPanel.add(exitButton);

        add(inputPanel, BorderLayout.NORTH);

        outputArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
        

        loadFileData();
        pack();
        setLocationRelativeTo(null);
    }

    private void loadFileData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COMPETITOR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Competitor competitor = parseCompetitor(line);
                if (competitor != null) {
                    competitorList.register_competitor(competitor);
                }
            }
            outputArea.append("\nCompetitor data loaded successfully!\n");
        } catch (IOException e) {
            outputArea.append("Error loading competitor data from file: " + e.getMessage() + "\n");
        }
    }

    private Competitor parseCompetitor(String line) {
        String[] parts = line.split(",");
        if (parts.length >= 7) {
            int compNum = Integer.parseInt(parts[0]);
            String name = parts[1];
            String email = parts[2];
            String dob = parts[3];
            String category = parts[4];
            String level = parts[5];

            Competitor competitor = new Competitor(compNum, name, email, dob, category, level);

            for (int i = 6; i < parts.length; i++) {
                competitor.addScore(Integer.parseInt(parts[i]));
            }

            return competitor;
        } else {
            outputArea.append("Invalid data format in competitor file: " + line + "\n");
            return null;
        }
    }

    private void saveCompetitorData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPETITOR_FILE))) {
            for (Competitor competitor : competitorList.getAllCompetitors()) {
                writer.write(competitorToFileString(competitor));
                writer.newLine();
            }
            outputArea.append("\nCompetitor data saved successfully!\n");
        } catch (IOException e) {
            outputArea.append("Error saving competitor data to file: " + e.getMessage() + "\n");
        }
    }

    private String competitorToFileString(Competitor competitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(competitor.getcomp_num()).append(",");
        sb.append(competitor.getcomp_name()).append(",");
        sb.append(competitor.getcomp_email()).append(",");
        sb.append(competitor.getcomp_DOB()).append(",");
        sb.append(competitor.getcomp_category()).append(",");
        sb.append(competitor.getcomp_level());

        for (int score : competitor.getScores()) {
            sb.append(",").append(score);
        }

        return sb.toString();
    }

    private void saveReportData() {
        String report = getReportString();
        try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
            writer.println(report);
            outputArea.append("\nReport data saved successfully!\n");
        } catch (IOException e) {
            outputArea.append("Error saving report data to file: " + e.getMessage() + "\n");
        }
    }

    private String getReportString() {
        StringBuilder report = new StringBuilder();

        report.append("\n===== Competition Report =====\n\n");
        report.append(getCompetitorTable());

        Competitor winner = competitorList.getWinner();
        report.append("\nCompetitor with Highest average score:-\n\n");
        if (winner != null) {
            report.append(winner.getFullDetails()).append("\n");
        } else {
            report.append("No competitors found.\n");
        }

        report.append("\nSummary Statistics:\n");
        report.append("Total Score: ").append(competitorList.getTotalScore()).append("\n");
        report.append("Average Score: ").append(competitorList.getAverageScore()).append("\n");
        report.append("Highest Score: ").append(competitorList.getMaxScore()).append("\n");
        report.append("Lowest Score: ").append(competitorList.getMinScore()).append("\n");

        report.append("\nFrequency Report:\n");
        Map<Integer, Integer> scoreFrequency = competitorList.getScoreFrequency();
        for (Map.Entry<Integer, Integer> entry : scoreFrequency.entrySet()) {
            report.append(String.format("Score %d: %d times%n", entry.getKey(), entry.getValue()));
        }
        return report.toString();
    }

    private String getCompetitorTable() {
        StringBuilder output = new StringBuilder();

        output.append("\nCompetitor Table:\n");
        output.append("--------------------------------------------------------------\n");
        output.append(String.format("%-6s %-20s %-15s %-10s %-18s %n", "No.", "Name", "Level", "Scores", "Average"));
        output.append("--------------------------------------------------------------\n");
        for (Competitor competitor : competitorList.getAllCompetitors()) {
            output.append(competitor.toString()).append("\n");
        }
        output.append("--------------------------------------------------------------\n\n");
        return output.toString();
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int compNum = Integer.parseInt(compNumField.getText());
                String name = compNameField.getText();
                String email = compEmailField.getText();
                String dob = compDOBField.getText();
                String category = compCategoryField.getText();
                String level = compLevelField.getText();

                Competitor newCompetitor = new Competitor(compNum, name, email, dob, category, level);
                boolean success = competitorList.register_competitor(newCompetitor);

                if (success) {
                    outputArea.append("Competitor registered successfully.\n");
                } else {
                    outputArea.append("Competitor with the same email and category already exists.\n");
                }
            } catch (NumberFormatException ex) {
                outputArea.append("Error: Invalid input for competitor.\n");
            }
        }
    }

    private class UpdateButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
    try {
    int compNum = Integer.parseInt(compNumField.getText());
    String name = compNameField.getText();
    String email = compEmailField.getText();
    String dob = compDOBField.getText();
    String category = compCategoryField.getText();
    String level = compLevelField.getText();
    Competitor updatedCompetitor = new Competitor(compNum, name, email, dob, category, level);
    boolean success = competitorList.update_comp_details(compNum, updatedCompetitor);

    if (success) {
        outputArea.append("Competitor details updated successfully.\n");
    } else {
        outputArea.append("Failed to update competitor details.\n");
    }
} catch (NumberFormatException ex) {
    outputArea.append("Error: Invalid input for competitor number.\n");
}
}
}

private class RemoveButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
try {
    int compNum = Integer.parseInt(compNumField.getText());
    boolean success = competitorList.remove_comp(compNum);

    if (success) {
        outputArea.append("Competitor removed successfully.\n");
    } else {
        outputArea.append("Failed to remove competitor.\n");
    }
} catch (NumberFormatException ex) {
    outputArea.append("Error: Invalid input for competitor number.\n");
}
}
}

private class ScoreButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
try {
    int compNum = Integer.parseInt(compNumField.getText());
    Competitor competitor = competitorList.getCompetitorByNumber(compNum);
    if (competitor != null) {
        List<Integer> scores = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String scoreInput = JOptionPane.showInputDialog("Enter score " + (i + 1) + ":");
            int score = Integer.parseInt(scoreInput);
            if (score < 0 || score > 5) {
                JOptionPane.showMessageDialog(null, "Invalid score. Please enter a score between 0 and 5.");
                return;
            }
            scores.add(score);
        }
        competitor.setScores(scores);
        outputArea.append("Scores awarded successfully.\n");
    } else {
        outputArea.append("Competitor not found.\n");
    }
} catch (NumberFormatException ex) {
    outputArea.append("Error: Invalid input for competitor number.\n");
}
}
}

private class ViewAllButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
outputArea.setText(getCompetitorTable());
}
}

private class ViewDetailsButtonListener implements ActionListener {
private String viewType;

public ViewDetailsButtonListener(String viewType) {
this.viewType = viewType;
}

@Override
public void actionPerformed(ActionEvent e) {
try {
    int compNum = Integer.parseInt(compNumField.getText());
    Competitor competitor = competitorList.getCompetitorByNumber(compNum);
    if (competitor != null) {
        if ("FULL".equalsIgnoreCase(viewType)) {
            outputArea.setText(competitor.getFullDetails());
        } else if ("SHORT".equalsIgnoreCase(viewType)) {
            outputArea.setText(competitor.getShortDetails());
        }
    } else {
        outputArea.append("Competitor not found.\n");
    }
} catch (NumberFormatException ex) {
    outputArea.append("Error: Invalid input for competitor number.\n");
}
}
}

private class ShowReportButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
outputArea.setText(getReportString());
}
}

private class SaveDataButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
saveCompetitorData();
}
}

private class SaveReportButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
saveReportData();
}
}

private class ExitButtonListener implements ActionListener {
@Override
public void actionPerformed(ActionEvent e) {
int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?",
        "Exit Program", JOptionPane.YES_NO_OPTION);

if (confirmed == JOptionPane.YES_OPTION) {
    System.exit(0);
}
}
}

public static void main(String[] args) {
SwingUtilities.invokeLater(new Runnable() {
@Override
public void run() {
    new CompetitorManagerGUI().setVisible(true);
}
});
}}

