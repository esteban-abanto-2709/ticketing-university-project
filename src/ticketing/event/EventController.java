package ticketing.event;

import ticketing.utils.ConsoleFormatter;

import java.util.List;

public class EventController {

    private final EventDAO eventDAO = new EventDAO();

    public boolean register(Event event) {
        if (event == null || event.getCode() == null || event.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido.");
            return false;
        }

        String normalized = normalizeCode(event.getCode());

        if (eventDAO.findByCode(normalized) != null) {
            ConsoleFormatter.printWarning("Ya existe un evento con ese código.");
            return false;
        }

        Event toSave = new Event(
                normalized,
                event.getName(),
                event.getDescription(),
                event.getDate(),
                event.getLocalCode(),
                event.getArtistCode(),
                event.getStatus()
        );

        return eventDAO.save(toSave);
    }

    public Event findByCode(String code) {
        if (code == null || code.isBlank()) return null;
        return eventDAO.findByCode(normalizeCode(code));
    }

    public List<Event> findAll() {
        return eventDAO.findAll();
    }

    public boolean update(Event event) {
        if (event == null || event.getCode() == null || event.getCode().isBlank()) {
            return false;
        }

        Event toUpdate = new Event(
                normalizeCode(event.getCode()),
                event.getName(),
                event.getDescription(),
                event.getDate(),
                event.getLocalCode(),
                event.getArtistCode(),
                event.getStatus()
        );

        return eventDAO.update(toUpdate);
    }

    public boolean delete(String code) {
        if (code == null || code.isBlank()) return false;
        return eventDAO.deleteByCode(normalizeCode(code));
    }

    public boolean exists(String code) {
        if (code == null || code.isBlank()) return false;
        return eventDAO.findByCode(normalizeCode(code)) != null;
    }

    public boolean hasEvents() {
        List<Event> all = eventDAO.findAll();
        return all != null && !all.isEmpty();
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }
}
