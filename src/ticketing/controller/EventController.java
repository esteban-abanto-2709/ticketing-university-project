package ticketing.controller;

import java.util.List;
import ticketing.dao.interfaces.EventDAO;
import ticketing.dao.temporary.EventDAOTemporary;
import ticketing.local.LocalController;
import ticketing.local.LocalDAO;
import ticketing.entities.Event;
import ticketing.entities.EventStatus;

public class EventController {

    private final EventDAO eventDAO;
    private final LocalController localController;

    public EventController() {
        this.eventDAO = new EventDAOTemporary();
        this.localController = new LocalController();
    }

    // Registrar un nuevo evento con validaciones
    public boolean registerEvento(Event event) {
        // Validaciones de negocio
        if (event == null) {
            return false;
        }

        if (event.getCode() == null || event.getCode().trim().isEmpty()) {
            return false;
        }

        if (event.getName() == null || event.getName().trim().isEmpty()) {
            return false;
        }

        if (event.getArtista() == null || event.getArtista().trim().isEmpty()) {
            return false;
        }

        if (event.getCodeLocal() == null || event.getCodeLocal().trim().isEmpty()) {
            return false;
        }

        if (event.getFecha() == null || event.getHora() == null) {
            return false;
        }

        // Verificar que el local existe
        if (!localController.exists(event.getCodeLocal().trim())) {
            return false;
        }

        return eventDAO.save(event);
    }

    // Obtener todos los eventos
    public List<Event> getAllEventos() {
        return eventDAO.findAll();
    }

    // Buscar evento por código
    public Event getEventByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return eventDAO.findByCode(code.trim());
    }

    // Buscar eventos por nombre
    public List<Event> searchEventosByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllEventos();
        }
        return eventDAO.findByName(name.trim());
    }

    // Buscar eventos por código de local
    public List<Event> getEventosByLocal(String codigoLocal) {
        if (codigoLocal == null || codigoLocal.trim().isEmpty()) {
            return getAllEventos();
        }
        return eventDAO.findByCodigoLocal(codigoLocal.trim());
    }

    // Buscar eventos por estado
    public List<Event> getEventosByEstado(EventStatus estado) {
        if (estado == null) {
            return getAllEventos();
        }
        return eventDAO.findByEstado(estado);
    }

    // Verificar si un código ya existe
    public boolean isCodeAlreadyExists(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return eventDAO.existsByCode(code.trim());
    }

    // Actualizar un evento
    public boolean updateEvento(Event event) {
        if (event == null || event.getCode() == null) {
            return false;
        }

        // Verificar que el local existe si se cambió
        if (event.getCodeLocal() != null && !event.getCodeLocal().trim().isEmpty()) {
            if (!localController.exists(event.getCodeLocal().trim())) {
                return false;
            }
        }

        return eventDAO.update(event);
    }

    // Reprogramar un evento (cambiar fecha/hora y estado)
    public boolean reprogramarEvento(String code, java.time.LocalDate nuevaFecha, java.time.LocalTime nuevaHora) {
        if (code == null || code.trim().isEmpty() || nuevaFecha == null || nuevaHora == null) {
            return false;
        }

        Event event = getEventByCode(code);
        if (event == null) {
            return false;
        }

        // Verificar que el evento puede ser reprogramado
        if (!event.puedeSerReprogramado()) {
            return false;
        }

        event.setFecha(nuevaFecha);
        event.setHora(nuevaHora);
        event.setEstado(EventStatus.REPROGRAMADO);

        return eventDAO.update(event);
    }

    // Cancelar un evento
    public boolean cancelarEvento(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        Event event = getEventByCode(code);
        if (event == null) {
            return false;
        }

        // Verificar que el evento puede ser cancelado
        if (!event.puedeSerCancelado()) {
            return false;
        }

        event.setEstado(EventStatus.CANCELADO);
        return eventDAO.update(event);
    }

    // Finalizar un evento (marcarlo como realizado)
    public boolean finalizarEvento(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        Event event = getEventByCode(code);
        if (event == null) {
            return false;
        }

        // Verificar que el evento puede ser finalizado
        if (!event.puedeSerFinalizado()) {
            return false;
        }

        event.setEstado(EventStatus.REALIZADO);
        return eventDAO.update(event);
    }

    // Eliminar un evento
    public boolean deleteEvento(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return eventDAO.deleteByCode(code.trim());
    }

    // Obtener estadísticas
    public int getTotalEventos() {
        return eventDAO.count();
    }

    public int getTotalEventosByEstado(EventStatus estado) {
        return eventDAO.countByEstado(estado);
    }

    // Verificar si hay eventos registrados
    public boolean hasEventos() {
        return getTotalEventos() > 0;
    }

    // Verificar si un local existe (método auxiliar para la vista)
    public boolean localExists(String codigoLocal) {
        if (codigoLocal == null || codigoLocal.trim().isEmpty()) {
            return false;
        }
        return localController.exists(codigoLocal.trim());
    }


    // Verificar si hay locales registrados
    public boolean hasLocals() {
        return localController.hasLocals();
    }
}
