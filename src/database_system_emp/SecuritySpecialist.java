package database_system_emp;

public class SecuritySpecialist extends Employee {

  public SecuritySpecialist(String name, String surName, String yearOfBirth) {
    super(name, surName, yearOfBirth, Groups.SECURITYSPECIALIST);
  }

  @Override
  public void performSkill() {
    ConsoleInterface ci = new ConsoleInterface();
    System.out.println("Run security analysis for which employee?");
    Employee target = ci.findEmployee();
    if (target == null)
      return;
    secSkill(target);
  }

  public void secSkill(Employee emp) {

    double cooperationSum = 0;
    int sizeSum = emp.getCooperation().size();

    for (CooperationQuality quality : emp.getCooperation().values()) {
      if (quality == CooperationQuality.BAD)
        cooperationSum--;
      else if (quality == CooperationQuality.GOOD)
        cooperationSum++;
    }

    double outsideFactor = sizeSum > 0 ? cooperationSum / sizeSum : 0;

    double[][] pBase = {{0.5, 0.4, 0.1}, {0.2, 0.6, 0.2}, {0.1, 0.4, 0.5}};

    double[][] P = new double[3][3];
    double sensitivity = 0.4;

    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        P[i][j] = pBase[i][j];
        if (outsideFactor > 0 && j == 2)
          P[i][j] += outsideFactor * sensitivity;
        if (outsideFactor < 0 && j == 0)
          P[i][j] += Math.abs(outsideFactor) * sensitivity;
      }
      double rowSum = P[i][0] + P[i][1] + P[i][2];
      for (int j = 0; j < 3; j++)
        P[i][j] /= rowSum;
    }

    double[] v = {0.0, 1.0, 0.0};
    for (int iter = 0; iter < 20; iter++) {
      double[] next = new double[3];
      for (int j = 0; j < 3; j++)
        for (int i = 0; i < 3; i++)
          next[j] += v[i] * P[i][j];
      v = next;
    }

    double score = v[0] * 1.0 + v[1] * 2.0 + v[2] * 3.0;
    System.out.printf("Risk score for %s %s: %.4f%n", emp.getName(),
                      emp.getSurName(), score);
    System.out.println("  (1.0 = highest risk / bad cooperations, "
                       + "3.0 = lowest risk / good cooperations)");
  }

  @Override
  public String getSkillName() {
    return "Employee security analysis";
  }
}
