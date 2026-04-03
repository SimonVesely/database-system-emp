package database_system_emp;

public class DataAnalyst extends Employee {
  public DataAnalyst(String name, String surName, String yearOfBirth) {
    super(name, surName, yearOfBirth, Groups.DataAnalyst);
  }

  @Override
  public void performSkill() {
    System.out.println("TODO");
  }

  @Override
  public String getSkillName() {
    return "Analys of most cooperating Employee";
  }
}
