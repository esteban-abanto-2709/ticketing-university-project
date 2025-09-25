package ticketing.dao.interfaces;

import java.util.List;
import ticketing.entities.Local;

public interface LocalDAO {
    
    // Crear un nuevo local
    boolean save(Local local);
    
    // Obtener todos los locales
    List<Local> findAll();
    
    // Buscar local por código
    Local findByCode(String code);
    
    // Buscar locales por nombre (búsqueda parcial)
    List<Local> findByName(String name);
    
    // Verificar si existe un código
    boolean existsByCode(String code);
    
    // Actualizar un local existente
    boolean update(Local local);
    
    // Eliminar un local por código
    boolean deleteByCode(String code);
    
    // Contar total de locales
    int count();
}