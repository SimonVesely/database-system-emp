package database_system_emp;

public class SecuritySpecialist extends Employee {
  public SecuritySpecialist(String name, String surName, String yearOfBirth) {
    super(name, surName, yearOfBirth, Groups.SECURITYSPECIALIST);
  }

  @Override
  public void performSkill() {
    ConsoleInterface ci = new ConsoleInterface();
    int employeeInfo = ci.getEmployeeInfo();
    secSkill(employeeInfo);
  }

public void secSkill(Integer id) {

  // Markov chain: https://en.wikipedia.org/wiki/Markov_chain 
    Employee emp = Main.TheList.get(id);
    double cooperationSum = 0;
    int sizeSum = emp.getCooperation().size();

    for (CooperationQuality quality : emp.getCooperation().values()) {
        if (quality == CooperationQuality.BAD) cooperationSum--;
        else if (quality == CooperationQuality.GOOD) cooperationSum++;
    }

    double outsideFactor = sizeSum > 0 ? (double) cooperationSum / sizeSum : 0;

    double[][] pBase = {
        {0.5, 0.4, 0.1},
        {0.2, 0.6, 0.2},
        {0.1, 0.4, 0.5}
    };

    double[][] P = new double[3][3];
    double sensitivity = 0.4;

    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            P[i][j] = pBase[i][j];
            
            if (outsideFactor > 0 && j == 2) P[i][j] += outsideFactor * sensitivity;
            if (outsideFactor < 0 && j == 0) P[i][j] += Math.abs(outsideFactor) * sensitivity;
        }
        
        double rowSum = P[i][0] + P[i][1] + P[i][2];
        for (int j = 0; j < 3; j++) {
            P[i][j] /= rowSum;
        }
    }

    double[] v = {0.0, 1.0, 0.0};
    
    for (int iter = 0; iter < 20; iter++) {
        double[] nextV = new double[3];
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                nextV[j] += v[i] * P[i][j];
            }
        }
        v = nextV;
    }
    double finalQuality = (v[0] * 1.0) + (v[1] * 2.0) + (v[2] * 3.0);

    System.out.println("Quality of " + emp.getName() + ": " + finalQuality);
}


  @Override
  public String getSkillName() {
    return "Security analysis of employees";
  }
}
