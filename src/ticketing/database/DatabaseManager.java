package ticketing.database;

import java.sql.*;

/**
 * DatabaseManager
 * ---------------------
 * Maneja la conexión general a la base de datos MySQL.
 * <p>
 * 🔧 NOTA: Si MySQL no arranca automáticamente en Windows,
 * puedes iniciarlo manualmente con:
 * <p>
 * net start MySQL80
 * <p>
 * Para verificar que está corriendo:
 * <p>
 * netstat -ano | find "3306"
 * <p>
 * Si luego deseas detenerlo:
 * <p>
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

                String URL = DatabaseConfig.SERVER_URL + DatabaseConfig.DB_NAME;
                connection = DriverManager.getConnection(URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
                System.out.println("[DatabaseManager] Conexión establecida con " + DatabaseConfig.DB_NAME);
            }
        } catch (Exception e) {
            System.err.println("[DatabaseManager] Error al conectar: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("[DatabaseManager] Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("[DatabaseManager] Error al cerrar la conexión: " + e.getMessage());
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
        } catch (SQLException e) {
            System.err.println("[DB] Error al ejecutar consulta: " + e.getMessage());
        }
    }

    public static ResultSet query(String sql, Object... params) {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            // ✅ Cierra el statement automáticamente cuando se cierre el ResultSet
            stmt.closeOnCompletion();

            return stmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("[DB] Error al ejecutar query: " + e.getMessage());
            return null;
        }
    }
}
