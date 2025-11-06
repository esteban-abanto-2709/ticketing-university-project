package ticketing.event.ticketType;

import ticketing.database.DatabaseManager;
import ticketing.utils.ConsoleFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class TicketTypeDAO {

    public boolean save(TicketType ticketType) {
        String sql = "INSERT INTO ticket_types (event_code, name, description, price) VALUES (?, ?, ?, ?)";
        try {
            DatabaseManager.execute(sql,
                    ticketType.getEventCode(),
                    ticketType.getName(),
                    ticketType.getDescription(),
                    ticketType.getPrice()
            );
            ConsoleFormatter.printDebug("[TicketTypeDAO] Ticket type saved successfully.");
            return true;
        } catch (Exception e) {
            System.err.println("[TicketTypeDAO] Error saving ticket type: " + e.getMessage());
            return false;
        }
    }

    public TicketType findTicketType(String eventCode, String name) {
        String sql = "SELECT * FROM ticket_types WHERE event_code = ? AND name = ?";
        try (ResultSet rs = DatabaseManager.query(sql, eventCode, name)) {
            if (rs != null && rs.next()) {
                return new TicketType(
                        rs.getString("event_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                );
            }
        } catch (SQLException e) {
            System.err.println("[TicketTypeDAO] Error fetching ticket type: " + e.getMessage());
        }
        return null;
    }

    public List<TicketType> findByEventCode(String eventCode) {
        String sql = "SELECT * FROM ticket_types WHERE event_code = ?";
        List<TicketType> ticketTypes = new ArrayList<>();
        try (ResultSet rs = DatabaseManager.query(sql, eventCode)) {
            while (rs != null && rs.next()) {
                ticketTypes.add(new TicketType(
                        rs.getString("event_code"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[TicketTypeDAO] Error fetching ticket types by event: " + e.getMessage());
        }
        return ticketTypes;
    }

    public boolean update(TicketType ticketType) {
        String sql = "UPDATE ticket_types SET description = ?, price = ? WHERE event_code = ? AND name = ?";
        try {
            DatabaseManager.execute(sql,
                    ticketType.getDescription(),
                    ticketType.getPrice(),
                    ticketType.getEventCode(),
                    ticketType.getName()
            );
            ConsoleFormatter.printDebug("[TicketTypeDAO] Ticket type updated successfully.");
            return true;
        } catch (Exception e) {
            System.err.println("[TicketTypeDAO] Error updating ticket type: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteTicketType(String eventCode, String name) {
        String sql = "DELETE FROM ticket_types WHERE event_code = ? AND name = ?";
        try {
            DatabaseManager.execute(sql, eventCode, name);
            ConsoleFormatter.printDebug("[TicketTypeDAO] Ticket type deleted successfully for event: " + eventCode);
            return true;
        } catch (Exception e) {
            System.err.println("[TicketTypeDAO] Error deleting ticket type: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAllByEvent(String eventCode) {
        String sql = "DELETE FROM ticket_types WHERE event_code = ?";
        try {
            DatabaseManager.execute(sql, eventCode);
            ConsoleFormatter.printDebug("[TicketTypeDAO] All ticket types deleted for event: " + eventCode);
            return true;
        } catch (Exception e) {
            System.err.println("[TicketTypeDAO] Error deleting all ticket types for event: " + e.getMessage());
            return false;
        }
    }

    public int countByEvent(String eventCode) {
        String sql = "SELECT COUNT(*) AS total FROM ticket_types WHERE event_code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, eventCode)) {
            if (rs != null && rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("[TicketTypeDAO] Error counting ticket types: " + e.getMessage());
        }
        return 0;
    }
}
