package ticketing.dao.interfaces;

import java.util.List;
import ticketing.entities.Event;
import ticketing.entities.EventStatus;

public interface EventDAO {

    // Crear un nuevo evento
    boolean save(Event event);

    // Obtener todos los eventos
    List<Event> findAll();

    // Buscar evento por código
    Event findByCode(String code);

    // Buscar eventos por código de local
    List<Event> findByCodigoLocal(String codigoLocal);

    // Buscar eventos por estado
    List<Event> findByEstado(EventStatus estado);

    // Buscar eventos por nombre (búsqueda parcial)
    List<Event> findByName(String name);

    // Verificar si existe un código
    boolean existsByCode(String code);

    // Actualizar un evento existente
    boolean update(Event event);

    // Eliminar un evento por código
    boolean deleteByCode(String code);

    // Contar total de eventos
    int count();

    // Contar eventos por estado
    int countByEstado(EventStatus estado);
}