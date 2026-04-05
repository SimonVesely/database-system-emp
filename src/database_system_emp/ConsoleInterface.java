package database_system_emp;
import java.util.Scanner;

public class ConsoleInterface{

    Scanner sc = new Scanner(System.in);

    public void addEmployee(){
        System.out.println("Enter a name of the new employee: ");
        String newName = sc.nextLine();

        System.out.println("Enter a surname of the new employee: ");
        String newSurName = sc.nextLine(); 

        System.out.println("Enter a year of birth of the new employee: ");
        String newYearOfBirth = sc.nextLine(); 

        System.out.println("Enter a role of the new employee (Security / Data): ");
        String newRole = sc.nextLine().toLowerCase();

        while (true){

        if(newRole.equals("security") ){
            SecuritySpecialist employee = new SecuritySpecialist(newName, newSurName, newYearOfBirth);
            Main.TheList.add(employee);
            break;
        }

        else if(newRole.equals("data") ){
            DataAnalyst employee = new DataAnalyst(newName, newSurName, newYearOfBirth);
            Main.TheList.add(employee);
            break;
        }

        else{
            System.out.println("You entered a wrong role, please choose security or data: ");
            newRole = sc.nextLine().toLowerCase();
            continue;

        }

        }

        System.out.println("Employee added successfully!");
        //Send a sql command to send a new employee into the database
        //delete the employee obj.
    }

    public int getEmployeeInfo(){
        System.out.println("Enter a name or ID of the employee you want to check: ");
        if(sc.hasNextInt()){
            int employeeInfo = sc.nextInt();
            return employeeInfo;
        }

        else{
            String employeeInfo = sc.nextLine();
            for (int i = 0; i < Main.TheList.size(); i++) {
                Employee emp = Main.TheList.get(i);
                if (emp.getName().equals(employeeInfo)) {
                    return i;
                }
            }
            System.out.println("Employee not found.");
            return -1;

        }

    }

}