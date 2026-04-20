package database_system_emp;

import java.io.File;
import java.sql.*;

public class Database {

  private static final String DB_NAME = "employee.db";
  private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

  public static void ensureDatabaseExists() {
    File db = new File(DB_NAME);

    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      createTables(conn);

      if (db.exists() && db.length() > 0) {
        loadAllEmployeesFromDb();
        loadAllCooperations();
      }

    } catch (Exception e) {
      System.out.println("Database unavailable.");
    }
  }

  private static void createTables(Connection conn) throws SQLException {

    String emp = """
        CREATE TABLE IF NOT EXISTS
        employees(id INTEGER PRIMARY KEY, name TEXT, surname TEXT,
                  year_of_birth TEXT, role TEXT);
        """;

        String coop = """
        CREATE TABLE IF NOT EXISTS
        cooperations(employee_id INTEGER, coworker_id INTEGER, quality TEXT);
        """;

        Statement st = conn.createStatement();
    st.execute(emp);
    st.execute(coop);
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL);
  }

  public static void addEmployeeToDb(int id, String name, String surname,
      String year, String role) {

    try (Connection conn = getConnection()) {

      PreparedStatement ps = conn.prepareStatement("INSERT INTO employees VALUES(?,?,?,?,?)");

      ps.setInt(1, id);
      ps.setString(2, name);
      ps.setString(3, surname);
      ps.setString(4, year);
      ps.setString(5, role);

      ps.executeUpdate();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveAllToDb() {

    try (Connection conn = getConnection()) {

      Statement st = conn.createStatement();
      st.execute("DELETE FROM employees");
      st.execute("DELETE FROM cooperations");

      for (Employee emp : Main.TheList) {

        PreparedStatement ps = conn.prepareStatement("INSERT INTO employees VALUES(?,?,?,?,?)");

        ps.setInt(1, emp.getId());
        ps.setString(2, emp.getName());
        ps.setString(3, emp.getSurName());
        ps.setString(4, emp.getYearOfBirth());
        ps.setString(5, emp.getGroup().name());
        ps.executeUpdate();

        for (Integer id : emp.getCooperation().keySet()) {

          PreparedStatement ps2 = conn.prepareStatement("INSERT INTO cooperations VALUES(?,?,?)");

          ps2.setInt(1, emp.getId());
          ps2.setInt(2, id);
          ps2.setString(3, emp.getCooperation().get(id).name());

          ps2.executeUpdate();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void loadAllEmployeesFromDb() {

    try (Connection conn = getConnection()) {

      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM employees");

      while (rs.next()) {

        int id = rs.getInt("id");
        String name = rs.getString("name");
        String sur = rs.getString("surname");
        String year = rs.getString("year_of_birth");
        String role = rs.getString("role");

        Employee emp;

        if (role.equals("DATAANALYST"))
          emp = new DataAnalyst(name, sur, year);
        else
          emp = new SecuritySpecialist(name, sur, year);

        emp.setId(id);
        Main.TheList.add(emp);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void loadAllCooperations() {

    try (Connection conn = getConnection()) {

      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM cooperations");

      while (rs.next()) {

        int a = rs.getInt("employee_id");
        int b = rs.getInt("coworker_id");

        CooperationQuality q = CooperationQuality.valueOf(rs.getString("quality"));

        Main.TheList.get(a).addCooperation(b, q);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
