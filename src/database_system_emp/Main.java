package database_system_emp;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

  public static Map<Integer, Employee> TheMap = new LinkedHashMap<>();

  public static void main(String[] args) {

    Database.ensureDatabaseExists();
    ConsoleInterface ci = new ConsoleInterface();

    boolean running = true;

    while (running) {

      System.out.println("""
          1  Add employee
          2  Add cooperation
          3  Remove employee
          4  Find employee
          5  Run skill
          6  Alphabetical listing
          7  Statistics
          8  Group count
          9  Save to file
          10 Load from file
          11 Exit
          """);

      String line = ci.sc.nextLine().trim();
      int choice;
      try {
        choice = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("Invalid choice.");
        continue;
      }

      switch (choice) {
        case 1 -> ci.addEmployee();
        case 2 -> ci.addCooperation();
        case 3 -> ci.removeEmployee();
        case 4 -> ci.showEmployeeInfo();
        case 5 -> ci.runSkill();
        case 6 -> ci.alphabeticalPrint();
        case 7 -> ci.statistics();
        case 8 -> ci.groupCount();
        case 9 -> ci.saveToFile();
        case 10 -> ci.loadFromFile();
        case 11 -> {
          Database.saveAllToDb();
          running = false;
        }
        default -> System.out.println("Invalid choice.");
      }
    }
  }
}
