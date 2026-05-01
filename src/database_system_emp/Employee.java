package database_system_emp;

import java.util.HashMap;
import java.util.Map;

public abstract class Employee implements Skill {

  private static int counter = 0;

  private int id;
  private String name;
  private String surName;
  private String yearOfBirth;
  private final Groups group;

  private Map<Integer, CooperationQuality> cooperation = new HashMap<>();

  public Employee(String name, String surName, String yearOfBirth,
                  Groups group) {
    this.id = counter++;
    this.name = name;
    this.surName = surName;
    this.yearOfBirth = yearOfBirth;
    this.group = group;
  }

  public static Employee findById(int id) { return Main.TheMap.get(id); }

  public int getId() { return id; }

  public String getName() { return name; }

  public String getSurName() { return surName; }

  public Groups getGroup() { return group; }

  public String getYearOfBirth() { return yearOfBirth; }

  public void setId(int id) {
    this.id = id;
    if (id >= counter) {
      counter = id + 1;
    }
  }

  public void setName(String name) { this.name = name; }

  public void setSurName(String surName) { this.surName = surName; }

  public void setYearOfBirth(String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public Map<Integer, CooperationQuality> getCooperation() {
    return cooperation;
  }

  public void addCooperation(int employeeId, CooperationQuality quality) {
    cooperation.put(employeeId, quality);
  }

  public void removeCooperation(int employeeId) {
    cooperation.remove(employeeId);
  }

  @Override public abstract void performSkill();

  @Override
  public String getSkillName() {
    return this.getClass().getSimpleName();
  }
}
