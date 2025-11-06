package ticketing.event.ticketType;

import ticketing.utils.ConsoleFormatter;

import java.util.List;

public class TicketTypeController {

    private final TicketTypeDAO ticketTypeDAO = new TicketTypeDAO();

    public boolean register(TicketType ticketType) {
        if (ticketType == null || ticketType.getEventCode() == null || ticketType.getName() == null) {
            ConsoleFormatter.printError("[TicketTypeController] Datos inválidos.");
            return false;
        }

        String eventCode = normalizeCode(ticketType.getEventCode());
        String name = normalizeName(ticketType.getName());

        if (exists(eventCode, name)) {
            ConsoleFormatter.printWarning("Ya existe un tipo de entrada con ese nombre para este evento.");
            return false;
        }

        TicketType toSave = new TicketType(
                eventCode,
                name,
                ticketType.getDescription(),
                ticketType.getPrice()
        );

        return ticketTypeDAO.save(toSave);
    }

    public TicketType findTicketType(String eventCode, String name) {
        if (eventCode == null || name == null) return null;
        return ticketTypeDAO.findTicketType(normalizeCode(eventCode), normalizeName(name));
    }

    public List<TicketType> findByEvent(String eventCode) {
        if (eventCode == null) return null;
        return ticketTypeDAO.findByEventCode(normalizeCode(eventCode));
    }

    public boolean update(TicketType ticketType) {
        if (ticketType == null || ticketType.getEventCode() == null || ticketType.getName() == null) {
            ConsoleFormatter.printError("[TicketTypeController] Datos inválidos para actualizar tipo de entrada.");
            return false;
        }

        TicketType toUpdate = new TicketType(
                normalizeCode(ticketType.getEventCode()),
                normalizeName(ticketType.getName()),
                ticketType.getDescription(),
                ticketType.getPrice()
        );

        return ticketTypeDAO.update(toUpdate);
    }

    public boolean deleteTicketType(String eventCode, String name) {
        if (eventCode == null || name == null) return false;
        return ticketTypeDAO.deleteTicketType(normalizeCode(eventCode), normalizeName(name));
    }

    public boolean deleteAllByEvent(String eventCode) {
        if (eventCode == null) return false;
        return ticketTypeDAO.deleteAllByEvent(normalizeCode(eventCode));
    }

    public boolean exists(String eventCode, String name) {
        return findTicketType(eventCode, name) != null;
    }

    public boolean hasTicketTypes(String eventCode) {
        List<TicketType> list = findByEvent(eventCode);
        return list != null && !list.isEmpty();
    }

    public int countByEvent(String eventCode) {
        if (eventCode == null || eventCode.isBlank()) return 0;
        return ticketTypeDAO.countByEvent(normalizeCode(eventCode));
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }

    private String normalizeName(String name) {
        return (name == null) ? null : name.trim();
    }
}
