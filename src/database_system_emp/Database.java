package database_system_emp;

import java.sql.*;

public class Database {

  private static final String DB_NAME = "employee.db";
  private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

  public static void ensureDatabaseExists() {
    try (Connection conn = DriverManager.getConnection(DB_URL)) {
      createTables(conn);
      loadAllEmployeesFromDb();
      loadAllCooperations();
    } catch (Exception e) {
      System.out.println(
          "SQL database unavailable — program runs without backup.");
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

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(
             "INSERT OR REPLACE INTO employees VALUES(?,?,?,?,?)")) {
      ps.setInt(1, id);
      ps.setString(2, name);
      ps.setString(3, surname);
      ps.setString(4, year);
      ps.setString(5, role);
      ps.executeUpdate();
    } catch (Exception e) {
      System.out.println("Error saving employee to DB: " + e.getMessage());
    }
  }

  public static void saveAllToDb() {

    try (Connection conn = getConnection();
         Statement st = conn.createStatement()) {

      st.execute("DELETE FROM employees");
      st.execute("DELETE FROM cooperations");

      for (Employee emp : Main.TheMap.values()) {

        try (PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO employees VALUES(?,?,?,?,?)")) {
          ps.setInt(1, emp.getId());
          ps.setString(2, emp.getName());
          ps.setString(3, emp.getSurName());
          ps.setString(4, emp.getYearOfBirth());
          ps.setString(5, emp.getGroup().name());
          ps.executeUpdate();
        }

        for (java.util.Map.Entry<Integer, CooperationQuality> entry :
             emp.getCooperation().entrySet()) {

          try (PreparedStatement ps2 = conn.prepareStatement(
                   "INSERT INTO cooperations VALUES(?,?,?)")) {
            ps2.setInt(1, emp.getId());
            ps2.setInt(2, entry.getKey()); // key = partner's employee ID
            ps2.setString(3, entry.getValue().name());
            ps2.executeUpdate();
          }
        }
      }

      System.out.println("All data saved to SQL database.");

    } catch (Exception e) {
      System.out.println("Error saving to DB: " + e.getMessage());
    }
  }

  public static void loadAllEmployeesFromDb() {

    try (Connection conn = getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM employees")) {

      while (rs.next()) {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String sur = rs.getString("surname");
        String year = rs.getString("year_of_birth");
        String role = rs.getString("role");

        Employee emp = role.equals("DATAANALYST")
                           ? new DataAnalyst(name, sur, year)
                           : new SecuritySpecialist(name, sur, year);

        emp.setId(id);
        Main.TheMap.put(id, emp);
      }

    } catch (Exception e) {
    }
  }

  public static void loadAllCooperations() {

    try (Connection conn = getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery("SELECT * FROM cooperations")) {

      while (rs.next()) {
        int empId = rs.getInt("employee_id");
        int coworkId = rs.getInt("coworker_id");
        CooperationQuality q =
            CooperationQuality.valueOf(rs.getString("quality"));

        Employee emp = Main.TheMap.get(empId);
        if (emp != null) {
          emp.addCooperation(coworkId, q);
        }
      }

    } catch (Exception e) {
    }
  }
}
