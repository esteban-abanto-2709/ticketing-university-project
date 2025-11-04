package ticketing.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabase() {
        createDatabaseIfNotExists();

        System.out.println("[DatabaseSetup] Creando tablas si no existen...");

        createLocalTable();
        createArtistTable();
        createSponsorTable();
        createEventTable();

        System.out.println("[DatabaseSetup] Tablas verificadas o creadas correctamente.");
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

    private static void createLocalTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS locals (
                        code VARCHAR(20) PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        address VARCHAR(150),
                        capacity INT NOT NULL
                    )
                """;
        try {
            DatabaseManager.execute(sql);
            System.out.println("[DatabaseSetup] Tabla 'locals' verificada o creada correctamente.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error al crear tabla 'locals': " + e.getMessage());
        }
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

    private static void createSponsorTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS sponsors (
                        code VARCHAR(20) PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        phone VARCHAR(20),
                        address VARCHAR(150)
                    )
                """;
        try {
            DatabaseManager.execute(sql);
            System.out.println("[DatabaseSetup] Tabla 'sponsors' verificada o creada correctamente.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error al crear tabla 'sponsors': " + e.getMessage());
        }
    }

    private static void createEventTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS events (
            code VARCHAR(20) PRIMARY KEY,
            name VARCHAR(100) NOT NULL,
            description TEXT,
            date VARCHAR(20) NOT NULL,
            local_code VARCHAR(20) NOT NULL,
            artist_code VARCHAR(20) NOT NULL,
            status VARCHAR(30) DEFAULT 'PROGRAMADO',
            FOREIGN KEY (local_code) REFERENCES locals(code),
            FOREIGN KEY (artist_code) REFERENCES artists(code)
        )
    """;
        try {
            DatabaseManager.execute(sql);
            System.out.println("[DatabaseSetup] Tabla 'events' verificada o creada correctamente.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error al crear tabla 'events': " + e.getMessage());
        }
    }
}
