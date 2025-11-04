package ticketing.event;

import ticketing.database.DatabaseManager;
import ticketing.interfaces.GenericDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class EventDAO implements GenericDAO<Event> {

    @Override
    public boolean save(Event entity) {
        String sql = "INSERT INTO events (code, name, description, date, local_code, artist_code, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            DatabaseManager.execute(sql,
                    entity.getCode(),
                    entity.getName(),
                    entity.getDescription(),
                    entity.getDate(),
                    entity.getLocalCode(),
                    entity.getArtistCode(),
                    entity.getStatus());
            return true;
        } catch (Exception e) {
            System.err.println("[EventDAO] Error saving event: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Event findByCode(String code) {
        String sql = "SELECT * FROM events WHERE code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, code)) {
            if (rs != null && rs.next()) {
                return mapResultSetToEvent(rs);
            }
        } catch (SQLException e) {
            System.err.println("[EventDAO] Error finding event by code: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Event> findAll() {
        String sql = "SELECT * FROM events";
        List<Event> events = new ArrayList<>();

        try (ResultSet rs = DatabaseManager.query(sql)) {
            while (rs != null && rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println("[EventDAO] Error listing events: " + e.getMessage());
        }

        return events;
    }

    @Override
    public boolean update(Event entity) {
        String sql = """
                    UPDATE events
                    SET name = ?, description = ?, date = ?, local_code = ?, artist_code = ?, status = ?
                    WHERE code = ?
                """;
        try {
            DatabaseManager.execute(sql,
                    entity.getName(),
                    entity.getDescription(),
                    entity.getDate(),
                    entity.getLocalCode(),
                    entity.getArtistCode(),
                    entity.getStatus(),
                    entity.getCode());
            return true;
        } catch (Exception e) {
            System.err.println("[EventDAO] Error updating event: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteByCode(String code) {
        String sql = "DELETE FROM events WHERE code = ?";
        try {
            DatabaseManager.execute(sql, code);
            return true;
        } catch (Exception e) {
            System.err.println("[EventDAO] Error deleting event: " + e.getMessage());
            return false;
        }
    }

    private Event mapResultSetToEvent(ResultSet rs) {
        try {
            return new Event(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getString("date"),
                    rs.getString("local_code"),
                    rs.getString("artist_code"),
                    rs.getString("status")
            );
        } catch (SQLException e) {
            System.err.println("[EventDAO] Error mapping event record: " + e.getMessage());
            return null;
        }
    }
}
