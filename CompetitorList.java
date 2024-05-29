import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompetitorList {

    private final List<Competitor> competitors;

    public CompetitorList() {
        this.competitors = new ArrayList<>();
    }

    // check if competitor with same Email & Category already exists
    private boolean competitorExists(Competitor new_comp) {
        return competitors.contains(new_comp);
    }

    // Register a new competitor
    public boolean register_competitor(Competitor new_comp) {
        if (competitorExists(new_comp)) {
            return false; // Registration failed as competitor with same Email & Category already exists
        } else {
            competitors.add(new_comp);
            return true; // Registered successfully
        }
    }

    // Competitor Removal
    public boolean remove_comp(int compNum) {
        Competitor competitorToRemove = getCompetitorByNumber(compNum);
        if (competitorToRemove != null) {
            competitors.remove(competitorToRemove);
            return true; // Competitor removed
        } else {
            return false; // Competitor not found
        }
    }

    // Update competitor details using competitor number
    public boolean update_comp_details(int compNum, Competitor updatedCompetitor) {
        Competitor existingCompetitor = getCompetitorByNumber(compNum);
        if (existingCompetitor != null) {
            // Update fields based on the new object
            existingCompetitor.setcomp_level(updatedCompetitor.getcomp_level());
            // Update other fields as required

            return true; // Competitor details updated
        } else {
            return false; // Competitor not found
        }
    }

    // get all competitors
    public List<Competitor> getAllCompetitors() {
        return competitors;
    }

    // get competitor details using competitor number
    public Competitor getCompetitorByNumber(int compNum) {
        return competitors.stream()
            .filter(competitor -> competitor.getcomp_num() == compNum)
            .findFirst()
            .orElse(null);
    }

    // Get Highest scorer / highest average
    public Competitor getWinner() {
        return competitors.stream()
            .max(Comparator.comparingDouble(Competitor::calculateOverallScore))
            .orElse(null);
    }

    // Report statistics (individual scores frequency)
    public Map<Integer, Integer> getScoreFrequency() {
        Map<Integer, Integer> scoreFrequency = new HashMap<>();

        for (Competitor competitor : competitors) {
            for (int score : competitor.getScores()) {
                scoreFrequency.put(score, scoreFrequency.getOrDefault(score, 0) + 1);
            }
        }

        return scoreFrequency;
    }

    // Total scores
    public int getTotalScore() {
        return competitors.stream()
            .flatMapToInt(competitor -> competitor.getScores().stream().mapToInt(Integer::intValue))
            .sum();
    }

    // Average score
    public double getAverageScore() {
        int totalScore = getTotalScore();
        int totalCompetitors = competitors.size();

        if (totalCompetitors > 0) {
          double average = (double) totalScore / totalCompetitors;
        return Double.parseDouble(String.format("%.2f", average));
        }
        else{
          return 0.0;
        }
    }

    // Highest score
    public int getMaxScore() {
        return competitors.stream()
            .flatMapToInt(competitor -> competitor.getScores().stream().mapToInt(Integer::intValue))
            .max()
            .orElse(0);
    }

    // Lowest score
    public int getMinScore() {
        return competitors.stream()
            .flatMapToInt(competitor -> competitor.getScores().stream().mapToInt(Integer::intValue))
            .min()
            .orElse(0);
    }
}