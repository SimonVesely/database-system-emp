package database_system_emp;

public class DataAnalyst extends Employee {
  public DataAnalyst(String name, String surName, String yearOfBirth) {
    super(name, surName, yearOfBirth, Groups.DATAANALYST);
  }

  @Override
  public void performSkill() {
    ConsoleInterface ci = new ConsoleInterface();
    int employeeInfo = ci.getEmployeeInfo();
    dataSkill(employeeInfo);
  }

  public int dataSkill(int id){

    Employee empA = Main.TheList.get(id);
    int sizeOfA = empA.getCooperation().size();
    int max = 0;
    int maxTemp = 0;
    int maxId = -1;

    for(int i = 0; i < sizeOfA; i++){
      int sizeOfB = Main.TheList.get(i).getCooperation().size();
      Employee empB = Main.TheList.get(i);

      if(sizeOfA >= sizeOfB){

        for(int j = 0; j < sizeOfA; j++){
          for(int k = 0; k < sizeOfB; k++){
            if(empA.getCooperation().get(j) == empB.getCooperation().get(k)){
              maxTemp++;
            }
          }
        }

      }

      else{

        for(int j = 0; j < sizeOfB; j++){
          for(int k = 0; k < sizeOfA; k++){
            if(empA.getCooperation().get(k) == empB.getCooperation().get(j)){
              maxTemp++;
            }
          }
        }

      }

      if(maxTemp > max){
        max = maxTemp;
        maxId = i;
      }

    }

    return maxId;

  }

  @Override
  public String getSkillName() {
    return "Analys of most cooperating Employee";
  }
}
