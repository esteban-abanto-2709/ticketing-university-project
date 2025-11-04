package ticketing.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabase() {
        createDatabaseIfNotExists();

        System.out.println("[DatabaseSetup] Creando tablas si no existen...");

        createArtistTable();

        System.out.println("[DatabaseSetup] Tablas verificadas o creadas correctamente.");
    }

    private static void createArtistTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS artists (
                code VARCHAR(20) PRIMARY KEY,
                name VARCHAR(100) NOT NULL
            )
        """;
        try {
            DatabaseManager.execute(sql);
            System.out.println("[DatabaseSetup] Tabla 'artists' verificada o creada correctamente.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error al crear tabla 'artists': " + e.getMessage());
        }
    }

    private static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(DatabaseConfig.SERVER_URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + DatabaseConfig.DB_NAME;
            stmt.executeUpdate(sql);
            System.out.println("[DatabaseSetup] Base de datos verificada o creada correctamente.");

        } catch (SQLException e) {
            System.err.println("[DatabaseSetup] No se pudo crear/verificar la base de datos: " + e.getMessage());
        }
    }
}
