package ticketing.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SampleDataLoader {

    public static void insertSampleData() {
        insertSampleLocals();
        insertSampleArtists();
        insertSampleSponsors();
        insertSampleEvents();
    }

    private static void insertSampleLocals() {
        String checkSql = "SELECT COUNT(*) AS total FROM locals";
        try (ResultSet rs = DatabaseManager.query(checkSql)) {
            if (rs != null && rs.next() && rs.getInt("total") == 0) {
                String insertSql = """
                            INSERT INTO locals (code, name, address, capacity) VALUES
                            ('L001', 'Estadio Nacional', 'Av. De la Victoria 1234, Lima', 45000),
                            ('L002', 'Arena Perú', 'Av. Javier Prado Este 4200, Lima', 15000),
                            ('L003', 'Teatro Municipal', 'Jr. Ica 377, Lima', 1200)
                        """;
                DatabaseManager.execute(insertSql);
                System.out.println("[SampleDataLoader] Datos de prueba para 'locals' insertados.");
            }
        } catch (SQLException e) {
            System.err.println("[SampleDataLoader] Error al insertar datos de prueba para 'locals': " + e.getMessage());
        }
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

    private static void insertSampleSponsors() {
        String checkSql = "SELECT COUNT(*) AS total FROM sponsors";
        try (ResultSet rs = DatabaseManager.query(checkSql)) {
            if (rs != null && rs.next() && rs.getInt("total") == 0) {
                String insertSql = """
                            INSERT INTO sponsors (code, name, phone, address) VALUES
                            ('S001', 'Coca-Cola', '987654321', 'Av. Los Olivos 123'),
                            ('S002', 'Samsung', '912345678', 'Jr. Las Flores 456'),
                            ('S003', 'Nike', '998877665', 'Calle Central 789')
                        """;
                DatabaseManager.execute(insertSql);
                System.out.println("[SampleDataLoader] Datos de prueba para 'sponsors' insertados.");
            }
        } catch (SQLException e) {
            System.err.println("[SampleDataLoader] Error al insertar datos de prueba de sponsors: " + e.getMessage());
        }
    }

    private static void insertSampleEvents() {
        String checkSql = "SELECT COUNT(*) AS total FROM events";
        try (ResultSet rs = DatabaseManager.query(checkSql)) {
            if (rs != null && rs.next() && rs.getInt("total") == 0) {
                String insertSql = """
                        INSERT INTO events (code, name, description, date, local_code, artist_code, status) VALUES
                        ('E001', 'Rock Legends Night', 'Concierto especial con The Rolling Stones.', '2025-12-15', 'L001', 'A001', 'ACTIVO'),
                        ('E002', 'Viva la Vida Tour', 'Coldplay en vivo con su gira mundial.', '2025-11-20', 'L002', 'A002', 'ACTIVO'),
                        ('E003', 'Adele Live Experience', 'Show íntimo de Adele con orquesta.', '2025-12-05', 'L003', 'A003', 'ACTIVO'),
                        ('E004', 'Classic Encore', 'Coldplay y Adele en presentación benéfica.', '2026-01-10', 'L002', 'A002', 'PENDIENTE')
                    """;
                DatabaseManager.execute(insertSql);
                System.out.println("[SampleDataLoader] Datos de prueba para 'events' insertados.");
            }
        } catch (SQLException e) {
            System.err.println("[SampleDataLoader] Error al insertar datos de prueba para 'events': " + e.getMessage());
        }
    }

}
