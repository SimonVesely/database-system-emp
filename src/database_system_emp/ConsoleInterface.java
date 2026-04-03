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

    }

}