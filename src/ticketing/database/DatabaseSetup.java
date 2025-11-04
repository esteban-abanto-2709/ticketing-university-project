package ticketing.database;

import ticketing.Config;
import ticketing.utils.ConsoleFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {

    public static void initializeDatabase() {
        createDatabaseIfNotExists();

        ConsoleFormatter.printDebug("[DatabaseSetup] Creating tables if they do not exist...");

        createLocalTable();
        createArtistTable();
        createSponsorTable();
        createEventTable();
        createZoneTable();

        ConsoleFormatter.printDebug("[DatabaseSetup] Tables verified or created successfully.");
    }

    private static void createDatabaseIfNotExists() {
        try (Connection conn = DriverManager.getConnection(Config.SERVER_URL, Config.USER, Config.PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + Config.DB_NAME;
            stmt.executeUpdate(sql);
            ConsoleFormatter.printDebug("[DatabaseSetup] Database verified or created successfully.");

        } catch (SQLException e) {
            System.err.println("[DatabaseSetup] Failed to create/verify database: " + e.getMessage());
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
            ConsoleFormatter.printDebug("[DatabaseSetup] Table 'locals' verified or created successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error creating table 'locals': " + e.getMessage());
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
            ConsoleFormatter.printDebug("[DatabaseSetup] Table 'artists' verified or created successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error creating table 'artists': " + e.getMessage());
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
            ConsoleFormatter.printDebug("[DatabaseSetup] Table 'sponsors' verified or created successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error creating table 'sponsors': " + e.getMessage());
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
            ConsoleFormatter.printDebug("[DatabaseSetup] Table 'events' verified or created successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error creating table 'events': " + e.getMessage());
        }
    }

    private static void createZoneTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS zones (
                        event_code VARCHAR(20) NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        capacity INT NOT NULL,
                        base_price DOUBLE NOT NULL,
                        PRIMARY KEY (event_code, name),
                        FOREIGN KEY (event_code) REFERENCES events(code)
                    )
                """;

        try {
            DatabaseManager.execute(sql);
            ConsoleFormatter.printDebug("[DatabaseSetup] Table 'zones' verified or created successfully.");
        } catch (Exception e) {
            System.err.println("[DatabaseSetup] Error creating table 'zones': " + e.getMessage());
        }
    }

}
