package database_system_emp;

import java.util.HashSet;
import java.util.Set;

public class DataAnalyst extends Employee {

  public DataAnalyst(String name, String surName, String yearOfBirth) {
    super(name, surName, yearOfBirth, Groups.DATAANALYST);
  }

  @Override
  public void performSkill() {
    ConsoleInterface ci = new ConsoleInterface();
    System.out.println("Analyse cooperations for which employee?");
    Employee target = ci.findEmployee();
    if (target == null) return;

    Employee best = dataSkill(target);

    if (best == null) {
      System.out.println(target.getName() + " " + target.getSurName()
          + " has no cooperations, or shares none with other employees.");
    } else {
      System.out.println("Employee with the most shared coworkers with "
          + target.getName() + " " + target.getSurName() + ": "
          + best.getName() + " " + best.getSurName()
          + " (ID=" + best.getId() + ")");
    }
  }

  public Employee dataSkill(Employee empA) {

    Set<Integer> coopA = empA.getCooperation().keySet();

    Employee bestMatch = null;
    int      bestCount = 0;

    for (Employee empB : Main.TheMap.values()) {

      if (empB.getId() == empA.getId()) continue; 

      Set<Integer> coopB = new HashSet<>(empB.getCooperation().keySet());
      coopB.retainAll(coopA);          
      int commonCount = coopB.size();

      if (commonCount > bestCount) {
        bestCount = commonCount;
        bestMatch = empB;
      }
    }

    return bestMatch;
  }

  @Override
  public String getSkillName() {
    return "Most shared coworker analysis";
  }
}
