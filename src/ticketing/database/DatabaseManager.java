package ticketing.database;

import ticketing.Config;
import ticketing.utils.ConsoleFormatter;

import java.sql.*;

/**
 * Note:
 * If MySQL does not start automatically on Windows, you can start it manually with:
 * net start MySQL80
 * <p>
 * To verify that it is running:
 * netstat -ano | find "3306"
 * <p>
 * To stop it:
 * net stop MySQL80
 */
public class DatabaseManager {

    private static Connection connection = null;

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");

                String URL = Config.SERVER_URL + Config.DB_NAME;
                connection = DriverManager.getConnection(URL, Config.USER, Config.PASSWORD);

                ConsoleFormatter.printDebug("[DatabaseManager] Connection established with " + Config.DB_NAME);
            }
        } catch (Exception e) {
            System.err.println("[DatabaseManager] Critical connection error: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                ConsoleFormatter.printDebug("[DatabaseManager] Connection closed successfully.");
            } catch (SQLException e) {
                System.err.println("[DatabaseManager] Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void execute(String sql, Object... params) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.executeUpdate();
            ConsoleFormatter.printDebug("[DatabaseManager] SQL executed successfully: " + sql);
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] SQL execution error: " + e.getMessage());
        }
    }

    public static ResultSet query(String sql, Object... params) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            stmt.closeOnCompletion(); // closes stmt automatically after ResultSet

            ConsoleFormatter.printDebug("[DatabaseManager] Query executed: " + sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Query execution error: " + e.getMessage());
            return null;
        }
    }
}
