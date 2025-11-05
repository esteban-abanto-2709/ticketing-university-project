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

        if (eventDAO.findByCode(event.getCode()) != null) {
            ConsoleFormatter.printWarning("Ya existe un evento con ese código.");
            return false;
        }

        return eventDAO.save(event);
    }

    public Event findByCode(String code) {
        if (code == null || code.isBlank()) return null;
        return eventDAO.findByCode(code);
    }

    public List<Event> findAll() {
        return eventDAO.findAll();
    }

    public boolean update(Event event) {
        if (event == null || event.getCode() == null || event.getCode().isBlank()) {
            return false;
        }

        return eventDAO.update(event);
    }

    public boolean delete(String code) {
        if (code == null || code.isBlank()) return false;
        return eventDAO.deleteByCode(code);
    }

    public boolean exists(String code) {
        if (code == null || code.isBlank()) return false;
        return eventDAO.findByCode(code) != null;
    }

    public boolean hasEvents() {
        List<Event> all = eventDAO.findAll();
        return all != null && !all.isEmpty();
    }
}
