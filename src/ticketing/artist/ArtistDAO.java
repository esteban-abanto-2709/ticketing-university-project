package ticketing.artist;

import ticketing.DatabaseManager;
import ticketing.interfaces.GenericDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO implements GenericDAO<Artist> {

    @Override
    public boolean save(Artist entity) {
        String sql = "INSERT INTO artists (code, name) VALUES (?, ?)";
        try {
            DatabaseManager.execute(sql, entity.getCode(), entity.getName());
            return true;
        } catch (Exception e) {
            System.err.println("[ArtistDAO] Error al guardar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Artist findByCode(String code) {
        String sql = "SELECT * FROM artists WHERE code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, code)) {
            if (rs != null && rs.next()) {
                Artist artist = new Artist(rs.getString("code"));
                artist.setName(rs.getString("name"));
                return artist;
            }
        } catch (SQLException e) {
            System.err.println("[ArtistDAO] Error al buscar: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Artist> findAll() {
        String sql = "SELECT * FROM artists";
        List<Artist> artists = new ArrayList<>();

        try (ResultSet rs = DatabaseManager.query(sql)) {
            while (rs != null && rs.next()) {
                Artist artist = new Artist(rs.getString("code"));
                artist.setName(rs.getString("name"));
                artists.add(artist);
            }
        } catch (SQLException e) {
            System.err.println("[ArtistDAO] Error al listar: " + e.getMessage());
        }

        return artists;
    }

    @Override
    public boolean update(Artist entity) {
        String sql = "UPDATE artists SET name = ? WHERE code = ?";
        try {
            DatabaseManager.execute(sql, entity.getName(), entity.getCode());
            return true;
        } catch (Exception e) {
            System.err.println("[ArtistDAO] Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByCode(String code) {
        String sql = "DELETE FROM artists WHERE code = ?";
        try {
            DatabaseManager.execute(sql, code);
            return true;
        } catch (Exception e) {
            System.err.println("[ArtistDAO] Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------------------
    // 🔧 Métodos temporales para desarrollo (eliminar en producción)
    // ------------------------------------------------------------

    public static void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS artists (
                    code VARCHAR(20) PRIMARY KEY,
                    name VARCHAR(100) NOT NULL
                )
                """;
        try {
            DatabaseManager.execute(sql);
            System.out.println("[ArtistDAO] Tabla 'artists' verificada.");
        } catch (Exception e) {
            System.err.println("[ArtistDAO] Error al crear tabla: " + e.getMessage());
        }
    }

    public static void insertSampleDataIfEmpty() {
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
                System.out.println("[ArtistDAO] Datos de prueba insertados.");
            }
        } catch (SQLException e) {
            System.err.println("[ArtistDAO] Error al insertar datos de prueba: " + e.getMessage());
        }
    }
}