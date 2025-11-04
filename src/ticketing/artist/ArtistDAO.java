package ticketing.artist;

import ticketing.database.DatabaseManager;
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
            System.err.println("[ArtistDAO] Error saving artist: " + e.getMessage());
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
            System.err.println("[ArtistDAO] Error finding artist: " + e.getMessage());
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
            System.err.println("[ArtistDAO] Error listing artists: " + e.getMessage());
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
            System.err.println("[ArtistDAO] Error updating artist: " + e.getMessage());
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
            System.err.println("[ArtistDAO] Error deleting artist: " + e.getMessage());
            return false;
        }
    }
}
