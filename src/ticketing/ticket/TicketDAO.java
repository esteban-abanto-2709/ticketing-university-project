package ticketing.ticket;

import ticketing.database.DatabaseManager;
import ticketing.utils.ConsoleFormatter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    private static final int BATCH_SIZE = 1000;  // Inserts por lote

    public boolean saveBatch(List<Ticket> tickets) {
        if (tickets == null || tickets.isEmpty()) {
            return false;
        }

        String sql = "INSERT INTO tickets (event_code, zone_name, ticket_number, type, status, sale_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int count = 0;
            for (Ticket ticket : tickets) {
                stmt.setString(1, ticket.getEventCode());
                stmt.setString(2, ticket.getZoneName());
                stmt.setInt(3, ticket.getTicketNumber());
                stmt.setString(4, ticket.getType());
                stmt.setString(5, ticket.getStatus());

                if (ticket.getSaleId() != null) {
                    stmt.setInt(6, ticket.getSaleId());
                } else {
                    stmt.setNull(6, java.sql.Types.INTEGER);
                }

                stmt.addBatch();
                count++;

                // Ejecutar cada 1000 registros
                if (count % BATCH_SIZE == 0) {
                    stmt.executeBatch();
                    ConsoleFormatter.printDebug("[TicketDAO] Batch executed: " + count + " tickets inserted");
                }
            }

            // Ejecutar el resto
            stmt.executeBatch();
            ConsoleFormatter.printDebug("[TicketDAO] Final batch executed. Total tickets: " + tickets.size());
            return true;

        } catch (Exception e) {
            System.err.println("[TicketDAO] Error saving batch tickets: " + e.getMessage());
            return false;
        }
    }

    public boolean save(Ticket ticket) {
        String sql = "INSERT INTO tickets (event_code, zone_name, ticket_number, type, status, sale_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            DatabaseManager.execute(sql,
                    ticket.getEventCode(),
                    ticket.getZoneName(),
                    ticket.getTicketNumber(),
                    ticket.getType(),
                    ticket.getStatus(),
                    ticket.getSaleId()
            );
            return true;
        } catch (Exception e) {
            System.err.println("[TicketDAO] Error saving ticket: " + e.getMessage());
            return false;
        }
    }

    public List<Ticket> findByEvent(String eventCode) {
        String sql = "SELECT * FROM tickets WHERE event_code = ?";
        List<Ticket> tickets = new ArrayList<>();

        try (ResultSet rs = DatabaseManager.query(sql, eventCode)) {
            while (rs != null && rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error listing tickets by event: " + e.getMessage());
        }

        return tickets;
    }

    public List<Ticket> findByEventAndZone(String eventCode, String zoneName) {
        String sql = "SELECT * FROM tickets WHERE event_code = ? AND zone_name = ?";
        List<Ticket> tickets = new ArrayList<>();

        try (ResultSet rs = DatabaseManager.query(sql, eventCode, zoneName)) {
            while (rs != null && rs.next()) {
                tickets.add(mapResultSetToTicket(rs));
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error listing tickets by event and zone: " + e.getMessage());
        }

        return tickets;
    }

    public int countByEvent(String eventCode) {
        String sql = "SELECT COUNT(*) AS total FROM tickets WHERE event_code = ?";
        try (ResultSet rs = DatabaseManager.query(sql, eventCode)) {
            if (rs != null && rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error counting tickets: " + e.getMessage());
        }
        return 0;
    }

    public int countByEventAndStatus(String eventCode, String status) {
        String sql = "SELECT COUNT(*) AS total FROM tickets WHERE event_code = ? AND status = ?";
        try (ResultSet rs = DatabaseManager.query(sql, eventCode, status)) {
            if (rs != null && rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error counting tickets by status: " + e.getMessage());
        }
        return 0;
    }

    public boolean deleteByEvent(String eventCode) {
        String sql = "DELETE FROM tickets WHERE event_code = ?";
        try {
            DatabaseManager.execute(sql, eventCode);
            ConsoleFormatter.printDebug("[TicketDAO] All tickets deleted for event: " + eventCode);
            return true;
        } catch (Exception e) {
            System.err.println("[TicketDAO] Error deleting tickets for event: " + e.getMessage());
            return false;
        }
    }

    private Ticket mapResultSetToTicket(ResultSet rs) {
        try {
            Ticket ticket = new Ticket(
                    rs.getString("event_code"),
                    rs.getString("zone_name"),
                    rs.getInt("ticket_number")
            );
            ticket.setType(rs.getString("type"));
            ticket.setStatus(rs.getString("status"));

            int saleId = rs.getInt("sale_id");
            if (!rs.wasNull()) {
                ticket.setSaleId(saleId);
            }

            return ticket;
        } catch (SQLException e) {
            System.err.println("[TicketDAO] Error mapping ticket record: " + e.getMessage());
            return null;
        }
    }
}