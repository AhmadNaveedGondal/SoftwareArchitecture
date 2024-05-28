import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Manager {

  private final CompetitorList competitorList;

  private static final String COMPETITOR_FILE = "RunCompetitor.csv";
  private static final String REPORT_FILE = "TheFinalReport.txt";

  public Manager() {
    this.competitorList = new CompetitorList();
    loadFileData();
  }

  // Switch case for main menu
  public void run() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      printMenu();
      String command = scanner.nextLine().trim();

      switch (command) {
        case "1":
          register_competitor(scanner);
          break;
        case "2":
          update_competitor(scanner);
          break;
        case "3":
          remove_competitor(scanner);
          break;
        case "4":
          give_scores(scanner);
          break;
        case "5":
          show_report();
          break;
        case "6":
          view_competitor_Info(scanner, "FULL");
          break;
        case "7":
          view_competitor_Info(scanner, "SHORT");
          break;
        case "8":
          show_competitor_table();
          break;
        case "9":
          save_comp_data();
          break;
        case "10":
          save_report_data();
          break;
          case "11":
          exit();
          break;
        default:
          System.out.println("\nInvalid command. \nSelect again.");
      }
            // Press enter to return to main menu
            System.out.println("\nPress Enter to return to Main Menu...");
            scanner.nextLine(); // Wait for user input
    }
  }

  //Menu Options
  private void printMenu() {
    System.out.println("\n===== Competitor Management System =====");
    System.out.println("1. Register Competitor ");
    System.out.println("2. Modify Competitor ");
    System.out.println("3. Remove Competitor ");
    System.out.println("4. Give Scores to Competitor");
    System.out.println("5. Generate Report");
    System.out.println("6. Get Competitor Details (Full)");
    System.out.println("7. Get Competitor Details (Short)");
    System.out.print("8. Show All Competitors (Tablular View)\n");
    System.out.print("9. Save changes in file\n");
    System.out.print("10. Save changes in report\n");
    System.out.println("11. Exit");
    
    System.out.print("\nEnter your Selection: ");
  }

  private void show_competitor_table() {
    // Print the complete output
    System.out.println(getCompetitorTable());
  }

  private String getCompetitorTable(){
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

  private void register_competitor(Scanner scanner) {
    System.out.println("\n===== Register Competitor =====");
    System.out.print("Enter Competitor Name: ");
    String comp_name = scanner.nextLine();

    System.out.print("Enter Competitor Email: ");
    String comp_email = scanner.nextLine();

    System.out.print("Enter D.O.B. (DD/MM/YYYY) ");
    String comp_DOB = scanner.nextLine();

    System.out.print("Enter Category: ");
    String comp_category = scanner.nextLine();

    System.out.print("Enter Level: ");
    String comp_level = scanner.nextLine();

    // Get the maximum competitor number
    int maxcomp_no = competitorList.getAllCompetitors().stream()
        .mapToInt(Competitor::getcomp_num)
        .max()
        .orElse(0);

    // Get new competitor number
    int comp_no = maxcomp_no + 1;

    Competitor newCompetitor = new Competitor(comp_no, comp_name, comp_email, comp_DOB, comp_category, comp_level);

    boolean registrationStatus = competitorList.register_competitor(newCompetitor);
    // Display registration result
    if (registrationStatus) {
      System.out.println("\nCompetitor Registered Successfully!");
    } else {
      System.out.println("Registration Failed. Competitor already exists with Email & Category.");
    }
  }

  // Modify Competitor
  private void update_competitor(Scanner scanner) {
    System.out.println("\n===== Competitor Modification =====");
    System.out.print("Enter Competitor No. ");
    int comp_no = scanner.nextInt();
    scanner.nextLine(); // Consume the newline character

    // Get competitor using CompetitorList
    Competitor existingCompetitor = competitorList.getCompetitorByNumber(comp_no);

    if (existingCompetitor != null) {
      // show existing competitor details
      System.out.println("Existing Details:");
      System.out.println(existingCompetitor.getFullDetails());

      // Ask user for updated details
      System.out.print("Enter Modified Name: ");
      String comp_name = scanner.nextLine();

      System.out.print("Enter New Email: ");
      String comp_email = scanner.nextLine();

      System.out.print("Enter New D.O.B. (DD/MM/YYYY) ");
      String comp_DOB = scanner.nextLine();

      System.out.print("Enter New Category: ");
      String comp_category = scanner.nextLine();

      System.out.print("Enter New Level: ");
      String comp_level = scanner.nextLine();

      // Create a Competitor object with updated details
      Competitor updated_comp = new Competitor(comp_no, comp_name, comp_email, comp_DOB, comp_category, comp_level);

      // Call update_competitorDetails method from CompetitorList
      boolean updateStatus = competitorList.update_comp_details(comp_no, updated_comp);

      // Display update result
      if (updateStatus) {
        System.out.println("Competitor Details Update Successfull!");
      } else {
        System.out.println("Update Failed. \nCompetitor unavailable.");
      }
    } else {
      System.out.println("Competitor unavailable. \nUpdate Failed.");
    }
  }

  // remove competitor
  private void remove_competitor(Scanner scanner) {
    System.out.println("\n===== Remove Competitor =====");
    System.out.print("Enter Competitor No. for Removal: ");
    int comp_no = scanner.nextInt();
    scanner.nextLine();

    // Call removeCompetitor method from CompetitorList
    boolean deleteStatus = competitorList.remove_comp(comp_no);

    // Display Removal Status
    if (deleteStatus) {
      System.out.println("\nCompetitor Removed successfully!");
    } else {
      System.out.println("Unable to remove competitor. Competitor unavailable.");
    }
  }

  // Full competitor details
  private void view_competitor_Info(Scanner scanner,String viewType) {
    System.out.println("\n===== Competitor Details =====");
    System.out.print("Competitor No. ");
    int comp_no = scanner.nextInt();
    scanner.nextLine();

    // Fetch the competitor using CompetitorList
    Competitor competitor = competitorList.getCompetitorByNumber(comp_no);

    // Display competitor information based on viewType
    if (competitor != null) {
      if ("full".equalsIgnoreCase(viewType)) {
        System.out.println(competitor.getFullDetails());
      } else if ("short".equalsIgnoreCase(viewType)) {
        System.out.println(competitor.getShortDetails());
      } else {
        System.out.println("Invalid view type. Please enter 'full'(Full Detailed Report) or 'short'(Short Report).");
      }
    } else {
      System.out.println("Competitor unavailable");
    }
  }

  // award scores to competitors
  private void give_scores(Scanner scanner) {
    System.out.println("\n===== Award Scores to Competitor =====");
    System.out.print("Enter Competitor No. ");
    int comp_no = scanner.nextInt();
    scanner.nextLine();

    Competitor competitor = competitorList.getCompetitorByNumber(comp_no);

    // Check if competitor already exists
    if (competitor != null) {
      // Prompt user for scores
      System.out.println("Award 5 scores between 0 and 5 :");
      List<Integer> scores = new ArrayList<>();
      for (int i = 0; i < 5; i++) {
        System.out.print("Enter score " + (i + 1) + ": ");
        int score = scanner.nextInt();
        // Validate score range
        if (score < 0 || score > 5) {
          System.out.println("Invalid score. \nAward score between 0 and 5.");
          i--;
          continue;
        }
        scores.add(score);
      }

      // Call addScore method
      competitor.setScores(scores);
      System.out.println("Scores awarded successfully.");
    } else {
      System.out.println("Competitor not found.");
    }
  }

  // print report
  private void show_report() {
    // Print the entire report
    System.out.println(getReportString());
  }

  private String getReportString(){
    StringBuilder report = new StringBuilder();

    // A table of competitors with full details
    report.append("\n===== Competition Report =====\n\n");
    report.append(getCompetitorTable());

    // Details of the competitor with the highest overall score
    Competitor winner = competitorList.getWinner();
    report.append("\nCompetitor with Highest average score:-\n\n");
    if (winner != null) {
      report.append(winner.getFullDetails()).append("\n");
    } else {
      report.append("No competitors found.\n");
    }

    // Four other summary statistics
    report.append("\nSummary Statistics:\n");
    report.append("Total Score: ").append(competitorList.getTotalScore()).append("\n");
    report.append("Average Score: ").append(competitorList.getAverageScore()).append("\n");
    report.append("Highest Score: ").append(competitorList.getMaxScore()).append("\n");
    report.append("Lowest Score: ").append(competitorList.getMinScore()).append("\n");

    // Frequency report
    report.append("\nFrequency Report:\n");
    Map<Integer, Integer> scoreFrequency = competitorList.getScoreFrequency();
    for (Map.Entry<Integer, Integer> entry : scoreFrequency.entrySet()) {
      report.append(String.format("Score %d: %d times%n", entry.getKey(), entry.getValue()));
    }
    return report.toString();
  }

  //=====================================File Handling========================================

  // Load competitor data from the file
  private void loadFileData() {
    try (BufferedReader reader = new BufferedReader(new FileReader(COMPETITOR_FILE))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // Parse the line and create Competitor objects
        Competitor competitor = parseCompetitor(line);
        if (competitor != null) {
          competitorList.register_competitor(competitor);
        }
      }
      System.out.println("\nCompetitor data loaded successfully!");
    } catch (IOException e) {
      System.out.println("Error loading competitor data from file: " + e.getMessage());
    }
  }

  // Parse a line from the file to create a Competitor object
  private Competitor parseCompetitor(String line) {
    String[] parts = line.split(",");
    if (parts.length >= 7) {
      int comp_no = Integer.parseInt(parts[0]);
      String comp_name = parts[1];
      String comp_email = parts[2];
      String comp_DOB = parts[3];
      String comp_category = parts[4];
      String comp_level = parts[5];

      Competitor competitor = new Competitor(comp_no, comp_name, comp_email, comp_DOB, comp_category, comp_level);

      // Add scores
      for (int i = 6; i < parts.length; i++) {
        competitor.addScore(Integer.parseInt(parts[i]));
      }

      return competitor;
    } else {
      System.out.println("Invalid data format in competitor file: " + line);
      return null;
    }
  }

  // Save competitor data to the file
  private void save_comp_data() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPETITOR_FILE))) {
      for (Competitor competitor : competitorList.getAllCompetitors()) {
        writer.write(competitorToFileString(competitor));
        writer.newLine();
      }
      System.out.println("\nCompetitor data saved successfully!");
    } catch (IOException e) {
      System.out.println("Error saving competitor data to file: " + e.getMessage());
    }
  }

  // Convert Competitor object to a string for writing to the file
  private String competitorToFileString(Competitor competitor) {
    StringBuilder sb = new StringBuilder();
    sb.append(competitor.getcomp_num()).append(",");
    sb.append(competitor.getcomp_name()).append(",");
    sb.append(competitor.getcomp_email()).append(",");
    sb.append(competitor.getcomp_DOB()).append(",");
    sb.append(competitor.getcomp_category()).append(",");
    sb.append(competitor.getcomp_level());

    // Add scores
    for (int score : competitor.getScores()) {
      sb.append(",").append(score);
    }

    return sb.toString();
  }

  // Save report data to the file
  private void save_report_data() {
    String report=getReportString();
    try (PrintWriter writer = new PrintWriter(new FileWriter(REPORT_FILE))) {
      writer.println(report);
      System.out.println("\nReport data saved successfully!");
    } catch (IOException e) {
      System.out.println("Error saving report data to file: " + e.getMessage());
    }
  }

  //====================================================================================


  private void exit() {
    System.out.println("Closing System...");
    System.exit(0);
  }

  public static void main(String[] args) {
    Manager manager = new Manager();
    manager.run();
  }
}
