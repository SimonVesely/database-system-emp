package database_system_emp;

import java.util.Scanner;

public class ConsoleInterface {

  Scanner sc = new Scanner(System.in);

  public void addEmployee() {

    System.out.print("Enter name: ");
    String name = sc.nextLine();

    System.out.print("Enter surname: ");
    String surName = sc.nextLine();

    System.out.print("Enter year of birth: ");
    String year = sc.nextLine();

    System.out.print("Enter role (security / data): ");
    String role = sc.nextLine().toLowerCase();

    while (true) {

      if (role.equals("security")) {

        Employee emp = new SecuritySpecialist(name, surName, year);

        Main.TheList.add(emp);

        Database.addEmployeeToDb(emp.getId(), emp.getName(), emp.getSurName(),
            emp.getYearOfBirth(), "SECURITYSPECIALIST");

        break;
      }

      else if (role.equals("data")) {

        Employee emp = new DataAnalyst(name, surName, year);

        Main.TheList.add(emp);

        Database.addEmployeeToDb(emp.getId(), emp.getName(), emp.getSurName(),
            emp.getYearOfBirth(), "DATAANALYST");

        break;
      }

      else {
        System.out.print("Wrong role. Enter security/data: ");
        role = sc.nextLine().toLowerCase();
      }
    }

    System.out.println("Employee added.");
  }

  public int getEmployeeInfo() {

    System.out.print("Enter employee ID or name: ");

    if (sc.hasNextInt()) {

      int id = sc.nextInt();
      sc.nextLine();

      if (id >= 0 && id < Main.TheList.size())
        return id;

      System.out.println("Employee not found.");
      return -1;
    }

    String name = sc.nextLine();

    for (int i = 0; i < Main.TheList.size(); i++) {

      if (Main.TheList.get(i).getName().equalsIgnoreCase(name)) {

        return i;
      }
    }

    System.out.println("Employee not found.");

    return -1;
  }

  public void addCooperation() {

    System.out.println("First employee:");
    int id1 = getEmployeeInfo();

    System.out.println("Second employee:");
    int id2 = getEmployeeInfo();

    if (id1 == -1 || id2 == -1)
      return;

    System.out.println("""
        1 BAD
        2 AVERAGE
        3 GOOD
        """);

    int choice = sc.nextInt();
    sc.nextLine();

    CooperationQuality q;

    if (choice == 1)
      q = CooperationQuality.BAD;
    else if (choice == 2)
      q = CooperationQuality.AVERAGE;
    else
      q = CooperationQuality.GOOD;

    Main.TheList.get(id1)
        .addCooperation(id2, q);

    Main.TheList.get(id2)
        .addCooperation(id1, q);

    System.out.println(
        "Cooperation added.");
  }

  public void removeEmployee() {

    int id = getEmployeeInfo();

    if (id == -1)
      return;

    Main.TheList.remove(id);

    for (Employee e : Main.TheList) {
      e.removeCooperation(id);
    }

    System.out.println("Employee removed.");
  }

  public void findEmployee() {

    int id = getEmployeeInfo();

    if (id == -1)
      return;

    Employee e = Main.TheList.get(id);

    System.out.println("ID: " + e.getId());

    System.out.println("Name: " + e.getName());

    System.out.println("Surname: " + e.getSurName());

    System.out.println("Year of birth: " + e.getYearOfBirth());

    System.out.println("Role: " + e.getGroup());

    System.out.println("Number of cooperations: " + e.getCooperation().size());
  }

  public void runSkill() {

    int id = getEmployeeInfo();

    if (id == -1)
      return;

    Main.TheList.get(id).performSkill();
  }

  public void alphabeticalPrint() {

    Main.TheList
        .stream()

        .sorted((a, b) -> a.getSurName().compareToIgnoreCase(b.getSurName()))

        .forEach(e -> System.out.println(e.getSurName() + " " + e.getName() +
            " | " + e.getGroup()));
  }

  public void statistics() {

    if (Main.TheList.isEmpty()) {
      System.out.println("No employees.");
      return;
    }

    Employee max = null;
    int maxLinks = -1;

    int bad = 0;
    int avg = 0;
    int good = 0;

    for (Employee e : Main.TheList) {

      if (e.getCooperation().size() > maxLinks) {

        maxLinks = e.getCooperation().size();

        max = e;
      }

      for (CooperationQuality q : e.getCooperation().values()) {

        if (q == CooperationQuality.BAD)
          bad++;

        else if (q == CooperationQuality.AVERAGE)
          avg++;

        else
          good++;
      }
    }

    System.out.println("Employee with most links: " + max.getName() + " " +
        max.getSurName());

    if (good >= avg && good >= bad)

      System.out.println("Dominant quality: GOOD");

    else if (avg >= bad)

      System.out.println("Dominant quality: AVERAGE");

    else

      System.out.println("Dominant quality: BAD");
  }

  public void groupCount() {

    int data = 0;
    int sec = 0;

    for (Employee e : Main.TheList) {

      if (e.getGroup() == Groups.DATAANALYST)
        data++;
      else
        sec++;
    }

    System.out.println("Data analysts: " + data);

    System.out.println("Security specialists: " + sec);
  }
}
