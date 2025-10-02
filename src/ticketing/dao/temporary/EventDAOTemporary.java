package ticketing.dao.temporary;

import java.util.ArrayList;
import java.util.List;
import ticketing.dao.interfaces.EventDAO;
import ticketing.entities.Event;
import ticketing.entities.EventStatus;

public class EventDAOTemporary implements EventDAO {

    private static List<Event> events = new ArrayList<>();

    @Override
    public boolean save(Event event) {
        // Verificar que el código no exista
        if (existsByCode(event.getCode())) {
            return false;
        }
        return events.add(event);
    }

    @Override
    public List<Event> findAll() {
        // Retorna copia para evitar modificaciones
        return new ArrayList<>(events);
    }

    @Override
    public Event findByCode(String code) {
        for (Event event : events) {
            if (event.getCode().equalsIgnoreCase(code)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public List<Event> findByCodigoLocal(String codigoLocal) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getCodeLocal().equalsIgnoreCase(codigoLocal)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public List<Event> findByEstado(EventStatus estado) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getEstado() == estado) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public List<Event> findByName(String name) {
        List<Event> result = new ArrayList<>();
        String searchName = name.toLowerCase();

        for (Event event : events) {
            if (event.getName().toLowerCase().contains(searchName)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public boolean existsByCode(String code) {
        return findByCode(code) != null;
    }

    @Override
    public boolean update(Event event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getCode().equalsIgnoreCase(event.getCode())) {
                events.set(i, event);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteByCode(String code) {
        return events.removeIf(event -> event.getCode().equalsIgnoreCase(code));
    }

    @Override
    public int count() {
        return events.size();
    }

    @Override
    public int countByEstado(EventStatus estado) {
        int count = 0;
        for (Event event : events) {
            if (event.getEstado() == estado) {
                count++;
            }
        }
        return count;
    }
}