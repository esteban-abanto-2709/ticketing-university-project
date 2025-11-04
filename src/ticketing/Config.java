package ticketing;

public class Config {

    // === Database Configuration ===
    public static final String DB_NAME = "ticketing_db";
    public static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    public static final String USER = "root";
    public static final String PASSWORD = "admin";

    // === Initialization Settings ===
    public static final boolean AUTO_CREATE_DB_AND_TABLES = true;  // Automatically create database and tables if missing
    public static final boolean AUTO_LOAD_SAMPLE_DATA = true;       // Automatically insert sample data on startup

    // === Logging Settings ===
    public static final boolean SHOW_DEBUG_LOGS = true;             // Enable or disable debug logs
}
