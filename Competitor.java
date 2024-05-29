import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Competitor {
    private int comp_num;
    private String comp_name;
    private String comp_email;
    private String comp_DOB;
    private String comp_category;
    private String comp_level;
    private List<Integer> scores;

    public Competitor(int comp_num, String comp_name, String comp_email, String comp_DOB, String comp_category, String comp_level) {
        this.comp_num = comp_num;
        this.comp_name = comp_name;
        this.comp_email = comp_email;
        this.comp_DOB = comp_DOB;
        this.comp_category = comp_category;
        this.comp_level = comp_level;
        this.scores = new ArrayList<>();
    }

    // Getters and Setters
    public int getcomp_num() {
        return comp_num;
    }

    public void setcomp_num(int comp_num) {
        this.comp_num = comp_num;
    }

    public String getcomp_name() {
        return comp_name;
    }

    public void setcomp_name(String comp_name) {
        this.comp_name = comp_name;
    }

    public String getcomp_email() {
        return comp_email;
    }

    public void setcomp_email(String comp_email) {
        this.comp_email = comp_email;
    }

    public String getcomp_DOB() {
        return comp_DOB;
    }

    public void setcomp_DOB(String comp_DOB) {
        this.comp_DOB = comp_DOB;
    }

    public String getcomp_category() {
        return comp_category;
    }

    public void setcomp_category(String comp_category) {
        this.comp_category = comp_category;
    }

    public String getcomp_level() {
        return comp_level;
    }

    public void setcomp_level(String comp_level) {
        this.comp_level = comp_level;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public void addScore(int score) {
        scores.add(score);
    }

    public double calculateOverallScore() {
        return scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%-5d %-20s %-15s", comp_num, comp_name, comp_level));

        result.append(" ");
        for (int score : scores) {
            result.append(score).append(" ");
        }

        result.append(String.format("   %.2f", calculateOverallScore()));

        return result.toString();
    }

    public String getFullDetails() {
        StringBuilder result = new StringBuilder();
        int age = AgeCalculator(comp_DOB);
        result.append(String.format("Competitor Number %d, Name %s, Aged %d.%n", comp_num, comp_name, age));
        result.append(String.format("%s is a %s and awarded scores: ", comp_name, comp_level));

        for (int score : scores) {
            result.append(score).append(",");
        }

        if (!scores.isEmpty()) {
            result.setLength(result.length() - 1); // Remove trailing comma
        }

        result.append(String.format("%nOverall score of %.2f.", calculateOverallScore()));

        return result.toString();
    }

    // Age calculator
    private int AgeCalculator(String comp_DOB) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(comp_DOB, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public String getShortDetails() {
        return String.format("CN %d (%s) scored overall score %.2f.", comp_num, getInitials(), calculateOverallScore());
    }

    // Method to get competitor initials
    private String getInitials() {
        StringBuilder initials = new StringBuilder();
        String[] nameParts = comp_name.split(" ");
        for (String part : nameParts) {
            initials.append(part.charAt(0));
        }
        return initials.toString().toUpperCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Competitor that = (Competitor) obj;
        return comp_email.equals(that.comp_email) && comp_category.equals(that.comp_category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comp_email, comp_category);
    }
}