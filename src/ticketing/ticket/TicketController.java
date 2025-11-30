package ticketing.ticket;

import ticketing.event.zone.Zone;
import ticketing.event.zone.ZoneController;
import ticketing.utils.ConsoleFormatter;

import java.util.ArrayList;
import java.util.List;

public class TicketController {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final ZoneController zoneController = new ZoneController();

    public boolean generateForEvent(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) {
            ConsoleFormatter.printError("Código de evento inválido.");
            return false;
        }

        int existingTickets = ticketDAO.countByEvent(eventCode);
        if (existingTickets > 0) {
            ConsoleFormatter.printWarning("Este evento ya tiene " + existingTickets + " entradas generadas.");
            return false;
        }

        List<Zone> zones = zoneController.findByEvent(eventCode);
        if (zones == null || zones.isEmpty()) {
            ConsoleFormatter.printError("El evento no tiene zonas configuradas. Configure zonas antes de generar entradas.");
            return false;
        }

        List<Ticket> allTickets = new ArrayList<>();
        int totalCapacity = 0;

        for (Zone zone : zones) {
            totalCapacity += zone.getCapacity();
            for (int i = 1; i <= zone.getCapacity(); i++) {
                Ticket ticket = new Ticket(eventCode, zone.getName(), i);
                allTickets.add(ticket);
            }
            ConsoleFormatter.printDebug("[TicketController] Prepared " + zone.getCapacity() + " tickets for zone: " + zone.getName());
        }

        ConsoleFormatter.printInfo("Insertando " + totalCapacity + " entradas en la base de datos...");
        ConsoleFormatter.printInfo("Esto puede tomar unos segundos, por favor espere...");

        if (ticketDAO.saveBatch(allTickets)) {
            ConsoleFormatter.printSuccess("Se generaron " + totalCapacity + " entradas exitosamente.");
            return true;
        } else {
            ConsoleFormatter.printError("Error al generar entradas.");
            return false;
        }
    }

    public int countByEvent(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) {
            return 0;
        }
        return ticketDAO.countByEvent(eventCode);
    }

    public int countAvailableByEvent(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) {
            return 0;
        }
        return ticketDAO.countByEventAndStatus(eventCode, "AVAILABLE");
    }

    public List<Ticket> findByEvent(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) {
            return null;
        }
        return ticketDAO.findByEvent(eventCode);
    }

    public boolean hasTickets(String eventCode) {
        return countByEvent(eventCode) > 0;
    }

    public boolean deleteByEvent(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) {
            return false;
        }
        return ticketDAO.deleteByEvent(eventCode);
    }

    public int countAvailableByZone(String eventCode, String zoneName) {
        if (eventCode == null || zoneName == null) return 0;
        return ticketDAO.countAvailableByZone(eventCode, zoneName);
    }

    public boolean sellTickets(String eventCode, String zoneName, String ticketType, int quantity, int saleId) {
        if (eventCode == null || zoneName == null || ticketType == null || quantity <= 0) {
            ConsoleFormatter.printError("Datos de venta inválidos.");
            return false;
        }

        // Verificar disponibilidad
        int available = ticketDAO.countAvailableByZone(eventCode, zoneName);
        if (available < quantity) {
            ConsoleFormatter.printError("No hay suficientes entradas disponibles. Solo quedan " + available + " entradas.");
            return false;
        }

        // Obtener tickets disponibles
        List<Ticket> availableTickets = ticketDAO.findAvailableByZone(eventCode, zoneName, quantity);

        if (availableTickets.size() < quantity) {
            ConsoleFormatter.printError("Error al obtener las entradas disponibles.");
            return false;
        }

        // Actualizar los tickets
        for (Ticket ticket : availableTickets) {
            ticket.setType(ticketType);
            ticket.setStatus("SOLD");
            ticket.setSaleId(saleId);
        }

        // Guardar cambios en batch
        if (ticketDAO.updateBatch(availableTickets)) {
            ConsoleFormatter.printSuccess("Venta registrada exitosamente. " + quantity + " entradas vendidas.");
            return true;
        } else {
            ConsoleFormatter.printError("Error al registrar la venta.");
            return false;
        }
    }

    public List<Ticket> findBySaleId(int saleId) {
        return ticketDAO.findBySaleId(saleId);
    }

    public boolean cancelSale(int saleId) {
        List<Ticket> tickets = ticketDAO.findBySaleId(saleId);

        if (tickets == null || tickets.isEmpty()) {
            ConsoleFormatter.printWarning("No se encontraron entradas con ese ID de venta.");
            return false;
        }

        // Liberar los tickets
        for (Ticket ticket : tickets) {
            ticket.setType(null);
            ticket.setStatus("AVAILABLE");
            ticket.setSaleId(null);
        }

        if (ticketDAO.updateBatch(tickets)) {
            ConsoleFormatter.printSuccess("Venta cancelada. " + tickets.size() + " entradas liberadas.");
            return true;
        } else {
            ConsoleFormatter.printError("Error al cancelar la venta.");
            return false;
        }
    }
}
