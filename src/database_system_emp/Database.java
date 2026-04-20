package database_system_emp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  private static final String DB_NAME = "employee.db";
  private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

  public static void dbExistCheck() {

    File dbFile = new File(DB_NAME);

    if (!dbFile.exists()) {

      try (Connection conn = DriverManager.getConnection(DB_URL)) {
      } catch (SQLException e) {
        e.printStackTrace();
      }

    }

  }
}
