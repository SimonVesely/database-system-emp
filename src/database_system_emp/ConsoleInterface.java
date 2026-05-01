package database_system_emp;

import java.io.*;
import java.util.*;

public class ConsoleInterface {

  Scanner sc = new Scanner(System.in);

  public void addEmployee() {

    System.out.print("First name: ");
    String name = sc.nextLine().trim();

    System.out.print("Last name: ");
    String surName = sc.nextLine().trim();

    System.out.print("Year of birth: ");
    String year = sc.nextLine().trim();

    System.out.print("Role (security / data): ");
    String role = sc.nextLine().trim().toLowerCase();

    while (!role.equals("security") && !role.equals("data")) {
      System.out.print("Invalid role. Enter 'security' or 'data': ");
      role = sc.nextLine().trim().toLowerCase();
    }

    Employee emp;
    if (role.equals("security")) {
      emp = new SecuritySpecialist(name, surName, year);
    } else {
      emp = new DataAnalyst(name, surName, year);
    }

    Main.TheMap.put(emp.getId(), emp);
    Database.addEmployeeToDb(emp.getId(), emp.getName(), emp.getSurName(),
                             emp.getYearOfBirth(), emp.getGroup().name());

    System.out.println("Employee added. ID = " + emp.getId());
  }

  public Employee findEmployee() {

    System.out.print("Enter employee ID or name: ");
    String input = sc.nextLine().trim();

    try {
      int id = Integer.parseInt(input);
      Employee emp = Main.TheMap.get(id);
      if (emp != null)
        return emp;
      System.out.println("No employee found with ID " + id + ".");
      return null;
    } catch (NumberFormatException ignored) {
    }

    List<Employee> matches = new ArrayList<>();
    for (Employee e : Main.TheMap.values()) {
      if (e.getName().equalsIgnoreCase(input) ||
          e.getSurName().equalsIgnoreCase(input)) {
        matches.add(e);
      }
    }

    if (matches.isEmpty()) {
      System.out.println("Employee not found.");
      return null;
    }

    if (matches.size() == 1)
      return matches.get(0);

    System.out.println("Multiple employees found:");
    for (int i = 0; i < matches.size(); i++) {
      Employee e = matches.get(i);
      System.out.printf("  %d) ID=%-4d %s %s%n", i + 1, e.getId(), e.getName(),
                        e.getSurName());
    }
    System.out.print("Select number: ");
    try {
      int pick = Integer.parseInt(sc.nextLine().trim());
      if (pick >= 1 && pick <= matches.size())
        return matches.get(pick - 1);
    } catch (NumberFormatException ignored) {
    }
    System.out.println("Invalid selection.");
    return null;
  }

  public void addCooperation() {

    System.out.println("First employee:");
    Employee e1 = findEmployee();

    System.out.println("Second employee:");
    Employee e2 = findEmployee();

    if (e1 == null || e2 == null)
      return;

    if (e1.getId() == e2.getId()) {
      System.out.println("An employee cannot cooperate with themselves.");
      return;
    }

    System.out.println("""
        Cooperation quality:
        1 BAD
        2 AVERAGE
        3 GOOD
        """);

    CooperationQuality q;
    try {
      int choice = Integer.parseInt(sc.nextLine().trim());
      if (choice == 1)
        q = CooperationQuality.BAD;
      else if (choice == 2)
        q = CooperationQuality.AVERAGE;
      else
        q = CooperationQuality.GOOD;
    } catch (NumberFormatException ex) {
      q = CooperationQuality.AVERAGE;
    }

    e1.addCooperation(e2.getId(), q);
    e2.addCooperation(e1.getId(), q);

    System.out.println("Cooperation added.");
  }

  public void removeEmployee() {

    Employee emp = findEmployee();
    if (emp == null)
      return;

    int removedId = emp.getId();

    for (Employee e : Main.TheMap.values()) {
      e.removeCooperation(removedId);
    }

    Main.TheMap.remove(removedId);
    System.out.println("Employee removed.");
  }

  public void showEmployeeInfo() {

    Employee e = findEmployee();
    if (e == null)
      return;

    System.out.println("ID:                 " + e.getId());
    System.out.println("First name:         " + e.getName());
    System.out.println("Last name:          " + e.getSurName());
    System.out.println("Year of birth:      " + e.getYearOfBirth());
    System.out.println("Role:               " + e.getGroup());
    System.out.println("Cooperations:       " + e.getCooperation().size());

    if (!e.getCooperation().isEmpty()) {
      System.out.println("Coworkers:");
      for (Map.Entry<Integer, CooperationQuality> entry :
           e.getCooperation().entrySet()) {
        Employee coworker = Main.TheMap.get(entry.getKey());
        String coworkerName =
            (coworker != null)
                ? coworker.getName() + " " + coworker.getSurName()
                : "[removed, ID=" + entry.getKey() + "]";
        System.out.printf("  - %-30s %s%n", coworkerName, entry.getValue());
      }
    }
  }

  public void runSkill() {
    Employee emp = findEmployee();
    if (emp == null)
      return;
    emp.performSkill();
  }

  public void alphabeticalPrint() {

    System.out.println("=== DATA ANALYSTS ===");
    Main.TheMap.values()
        .stream()
        .filter(e -> e.getGroup() == Groups.DATAANALYST)
        .sorted(
            Comparator
                .comparing(Employee::getSurName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Employee::getName,
                               String.CASE_INSENSITIVE_ORDER))
        .forEach(e
                 -> System.out.printf("  ID=%-4d %s %s%n", e.getId(),
                                      e.getSurName(), e.getName()));

    System.out.println("=== SECURITY SPECIALISTS ===");
    Main.TheMap.values()
        .stream()
        .filter(e -> e.getGroup() == Groups.SECURITYSPECIALIST)
        .sorted(
            Comparator
                .comparing(Employee::getSurName, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Employee::getName,
                               String.CASE_INSENSITIVE_ORDER))
        .forEach(e
                 -> System.out.printf("  ID=%-4d %s %s%n", e.getId(),
                                      e.getSurName(), e.getName()));
  }

  public void statistics() {

    if (Main.TheMap.isEmpty()) {
      System.out.println("No employees.");
      return;
    }

    Employee maxEmployee = null;
    int maxLinks = -1;
    int bad = 0, avg = 0, good = 0;

    for (Employee e : Main.TheMap.values()) {

      if (e.getCooperation().size() > maxLinks) {
        maxLinks = e.getCooperation().size();
        maxEmployee = e;
      }

      for (Map.Entry<Integer, CooperationQuality> entry :
           e.getCooperation().entrySet()) {
        if (e.getId() < entry.getKey()) {
          switch (entry.getValue()) {
          case BAD -> bad++;
          case AVERAGE -> avg++;
          case GOOD -> good++;
          }
        }
      }
    }

    System.out.println("Employee with most links: " + maxEmployee.getName() +
                       " " + maxEmployee.getSurName() + " (" + maxLinks +
                       " links)");

    if (good >= avg && good >= bad)
      System.out.println("Dominant cooperation quality: GOOD");
    else if (avg >= bad)
      System.out.println("Dominant cooperation quality: AVERAGE");
    else
      System.out.println("Dominant cooperation quality: BAD");
  }

  public void groupCount() {

    long data = Main.TheMap.values()
                    .stream()
                    .filter(e -> e.getGroup() == Groups.DATAANALYST)
                    .count();
    long sec = Main.TheMap.values()
                   .stream()
                   .filter(e -> e.getGroup() == Groups.SECURITYSPECIALIST)
                   .count();

    System.out.println("Data analysts:         " + data);
    System.out.println("Security specialists:  " + sec);
  }

  public void saveToFile() {

    System.out.println("What do you want to save?");
    System.out.println("1 A single employee");
    System.out.println("2 All employees");

    String choice = sc.nextLine().trim();

    System.out.print("File name (without extension): ");
    String fileName = sc.nextLine().trim() + ".txt";

    List<Employee> toSave = new ArrayList<>();

    if (choice.equals("1")) {
      Employee emp = findEmployee();
      if (emp == null)
        return;
      toSave.add(emp);
    } else {
      toSave.addAll(Main.TheMap.values());
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
      for (Employee emp : toSave) {
        pw.println("ID=" + emp.getId());
        pw.println("NAME=" + emp.getName());
        pw.println("SURNAME=" + emp.getSurName());
        pw.println("YEAR=" + emp.getYearOfBirth());
        pw.println("ROLE=" + emp.getGroup().name());

        StringBuilder coopLine = new StringBuilder("COOPERATIONS=");
        for (Map.Entry<Integer, CooperationQuality> entry :
             emp.getCooperation().entrySet()) {
          coopLine.append(entry.getKey())
              .append(":")
              .append(entry.getValue())
              .append(";");
        }
        pw.println(coopLine);
        pw.println("---");
      }
      System.out.println("Saved to " + fileName + " (" + toSave.size() +
                         " employee(s)).");
    } catch (IOException e) {
      System.out.println("Error writing file: " + e.getMessage());
    }
  }

  public void loadFromFile() {

    System.out.print("File name (without extension): ");
    String fileName = sc.nextLine().trim() + ".txt";

    File file = new File(fileName);
    if (!file.exists()) {
      System.out.println("File '" + fileName + "' not found.");
      return;
    }

    int added = 0;
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      String line;
      int id = -1;
      String name = null, surname = null, year = null, role = null;
      Map<Integer, CooperationQuality> coops = new HashMap<>();

      while ((line = br.readLine()) != null) {
        line = line.trim();

        if (line.equals("---")) {
          if (id >= 0 && name != null && role != null) {
            if (!Main.TheMap.containsKey(id)) {
              Employee emp = role.equals("DATAANALYST")
                                 ? new DataAnalyst(name, surname, year)
                                 : new SecuritySpecialist(name, surname, year);
              emp.setId(id);
              for (Map.Entry<Integer, CooperationQuality> entry :
                   coops.entrySet()) {
                emp.addCooperation(entry.getKey(), entry.getValue());
              }
              Main.TheMap.put(id, emp);
              added++;
            }
          }
          id = -1;
          name = null;
          surname = null;
          year = null;
          role = null;
          coops = new HashMap<>();
          continue;
        }

        if (line.startsWith("ID="))
          id = Integer.parseInt(line.substring(3));
        else if (line.startsWith("NAME="))
          name = line.substring(5);
        else if (line.startsWith("SURNAME="))
          surname = line.substring(8);
        else if (line.startsWith("YEAR="))
          year = line.substring(5);
        else if (line.startsWith("ROLE="))
          role = line.substring(5);
        else if (line.startsWith("COOPERATIONS=")) {
          String coopStr = line.substring(13);
          if (!coopStr.isEmpty()) {
            for (String pair : coopStr.split(";")) {
              if (pair.isEmpty())
                continue;
              String[] parts = pair.split(":");
              if (parts.length == 2) {
                try {
                  int coopId = Integer.parseInt(parts[0]);
                  CooperationQuality q = CooperationQuality.valueOf(parts[1]);
                  coops.put(coopId, q);
                } catch (Exception ex) {
                }
              }
            }
          }
        }
      }

      System.out.println("Loaded " + added + " new employee(s) from '" +
                         fileName + "'.");

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }
}
