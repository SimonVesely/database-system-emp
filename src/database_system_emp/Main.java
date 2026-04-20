package database_system_emp;

import java.util.ArrayList;

public class Main {

  public static ArrayList<Employee> TheList = new ArrayList<>();

  public static void main(String[] args) {

    Database.ensureDatabaseExists();
    ConsoleInterface consoleInterface = new ConsoleInterface();

    boolean running= true;

    while (running) {

      System.out.println("""
          1 Add employee
          2 Add cooperation
          3 Remove employee
          4 Find employee
          5 Run skill
          6 Alphabetical print
          7 Statistics
          8 Group count
          9 Exit
          """);

      int choice = consoleInterface.sc.nextInt();
      consoleInterface.sc.nextLine();

      switch (choice) {
        case 1 -> consoleInterface.addEmployee();
        case 2 -> consoleInterface.addCooperation();
        case 3 -> consoleInterface.removeEmployee();
        case 4 -> consoleInterface.findEmployee();
        case 5 -> consoleInterface.runSkill();
        case 6 -> consoleInterface.alphabeticalPrint();
        case 7 -> consoleInterface.statistics();
        case 8 -> consoleInterface.groupCount();

        case 9 -> {
          Database.saveAllToDb();
          running = false;
        }

        default -> System.out.println("Wrong choice");
      }
    }
  }
}
