package ticketing.event.zone;

import ticketing.database.DatabaseManager;
import ticketing.utils.ConsoleFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ZoneDAO {

    public boolean save(Zone zone) {
        String sql = "INSERT INTO zones (event_code, name, capacity, base_price) VALUES (?, ?, ?, ?)";
        try {
            DatabaseManager.execute(sql,
                    zone.getEventCode(),
                    zone.getName(),
                    zone.getCapacity(),
                    zone.getBasePrice()
            );
            ConsoleFormatter.printDebug("[ZoneDAO] Zone saved successfully.");
            return true;
        } catch (Exception e) {
            System.err.println("[ZoneDAO] Error saving zone: " + e.getMessage());
            return false;
        }
    }

    public Zone findZone(String eventCode, String name) {
        String sql = "SELECT * FROM zones WHERE event_code = ? AND name = ?";
        try (ResultSet rs = DatabaseManager.query(sql, eventCode, name)) {
            if (rs != null && rs.next()) {
                return new Zone(
                        rs.getString("event_code"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getDouble("base_price")
                );
            }
        } catch (SQLException e) {
            System.err.println("[ZoneDAO] Error fetching zone: " + e.getMessage());
        }
        return null;
    }

    public List<Zone> findByEventCode(String eventCode) {
        String sql = "SELECT * FROM zones WHERE event_code = ?";
        List<Zone> zones = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.query(sql, eventCode)) {
            while (rs != null && rs.next()) {
                zones.add(new Zone(
                        rs.getString("event_code"),
                        rs.getString("name"),
                        rs.getInt("capacity"),
                        rs.getDouble("base_price")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[ZoneDAO] Error fetching zones by event: " + e.getMessage());
        }
        return zones;
    }

    public boolean update(Zone zone) {
        String sql = "UPDATE zones SET capacity = ?, base_price = ? WHERE event_code = ? AND name = ?";
        try {
            DatabaseManager.execute(sql,
                    zone.getCapacity(),
                    zone.getBasePrice(),
                    zone.getEventCode(),
                    zone.getName()
            );
            ConsoleFormatter.printDebug("[ZoneDAO] Zone updated successfully.");
            return true;
        } catch (Exception e) {
            System.err.println("[ZoneDAO] Error updating zone: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteZone(String eventCode, String name) {
        String sql = "DELETE FROM zones WHERE event_code = ? AND name = ?";
        try {
            DatabaseManager.execute(sql, eventCode, name);
            ConsoleFormatter.printDebug("[ZoneDAO] Zone deleted successfully for event: " + eventCode);
            return true;
        } catch (Exception e) {
            System.err.println("[ZoneDAO] Error deleting zone: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAllByEvent(String eventCode) {
        String sql = "DELETE FROM zones WHERE event_code = ?";
        try {
            DatabaseManager.execute(sql, eventCode);
            ConsoleFormatter.printDebug("[ZoneDAO] All zones deleted for event: " + eventCode);
            return true;
        } catch (Exception e) {
            System.err.println("[ZoneDAO] Error deleting all zones for event: " + e.getMessage());
            return false;
        }
    }

    public int getTotalCapacity(String eventCode) {
        String sql = "SELECT COALESCE(SUM(capacity), 0) AS total FROM zones WHERE event_code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, eventCode)) {
            if (rs != null && rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("[ZoneDAO] Error summing capacity: " + e.getMessage());
        }
        return 0;
    }
}
