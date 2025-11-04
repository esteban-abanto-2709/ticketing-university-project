package ticketing.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SampleDataLoader {

    public static void insertSampleData() {
        insertSampleArtists();
    }

    private static void insertSampleArtists() {
        String checkSql = "SELECT COUNT(*) AS total FROM artists";
        try (ResultSet rs = DatabaseManager.query(checkSql)) {
            if (rs != null && rs.next() && rs.getInt("total") == 0) {
                String insertSql = """
                    INSERT INTO artists (code, name) VALUES
                    ('A001', 'The Rolling Stones'),
                    ('A002', 'Coldplay'),
                    ('A003', 'Adele')
                """;
                DatabaseManager.execute(insertSql);
                System.out.println("[SampleDataLoader] Datos de prueba para 'artists' insertados.");
            }
        } catch (SQLException e) {
            System.err.println("[SampleDataLoader] Error al insertar datos de prueba: " + e.getMessage());
        }
    }
}
