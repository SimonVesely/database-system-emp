package database_system_emp;

import java.util.HashMap;
import java.util.Map;

public abstract class Employee {

  private static int counter = 0;

  private int id;
  private String name;
  private String surName;
  private String yearOfBirth;
  private Map<Integer, Integer> cooperation;
  private Groups group;

  public Employee(String name, String surName, String yearOfBirth,
      Groups group) {

    this.id = counter++;
    this.name = name;
    this.surName = surName;
    this.yearOfBirth = yearOfBirth;
    this.cooperation = new HashMap<>();
    this.group = group;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getSurName() {
    return surName;
  }

  public Groups getGroup() {
    return group;
  }

  public String getYearOfBirth() {
    return yearOfBirth;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSurName(String surName) {
    this.surName = surName;
  }

  public void setYearOfBirth(String yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  public void addCooperation(int id, int quality) {
    cooperation.put(id, quality);
  }
}
