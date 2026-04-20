package database_system_emp;
import java.util.ArrayList;

public class Main {

  public static ArrayList<Employee> TheList = new ArrayList<Employee>();

  public static void main(String[] args) {
    Database.dbExistCheck();
    System.out.print("Template pro projekt");
  }
}
