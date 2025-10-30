package ticketing;

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

    private static final String DB_NAME = "ticketing_db";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private static Connection connection = null;

    private DatabaseManager() {
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Registrar driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Conectarse directamente a la base (ya existente)
                String URL = SERVER_URL + DB_NAME;
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DatabaseManager] Conexión establecida con " + DB_NAME);
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

    // ------------------------------------------------------------
    // 🔧 Métodos temporales para desarrollo (eliminar en producción)
    // ------------------------------------------------------------

    public static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            stmt.executeUpdate(sql);
            System.out.println("[DatabaseManager] Base de datos verificada o creada correctamente.");

        } catch (SQLException e) {
            System.err.println("[DatabaseManager] No se pudo crear/verificar la base de datos: " + e.getMessage());
        }
    }
}
