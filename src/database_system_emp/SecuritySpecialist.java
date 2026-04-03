package database_system_emp;

public class SecuritySpecialist extends Employee {
  public SecuritySpecialist(String name, String surName, String yearOfBirth) {
    super(name, surName, yearOfBirth, Groups.SecuritySpecialist);
  }

  @Override
  public void performSkill() {
    System.out.println("TODO");
  }

  @Override
  public String getSkillName() {
    return "Security analys of employees";
  }
}
