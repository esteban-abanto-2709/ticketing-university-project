package ticketing.services;

import java.util.List;
import ticketing.dao.interfaces.LocalDAO;
import ticketing.dao.temporary.LocalDAOTemporary;
import ticketing.entities.Local;

public class LocalService {

    private LocalDAO localDAO;

    public LocalService() {
        this.localDAO = new LocalDAOTemporary(); // Aquí cambiarás por BD cuando sea necesario
    }

    // Registrar un nuevo local con validaciones
    public boolean registerLocal(Local local) {
        // Validaciones de negocio
        if (local == null) {
            return false;
        }

        if (local.getCodigo() == null || local.getCodigo().trim().isEmpty()) {
            return false;
        }

        if (local.getNombre() == null || local.getNombre().trim().isEmpty()) {
            return false;
        }

        return localDAO.save(local);
    }

    // Obtener todos los locales
    public List<Local> getAllLocals() {
        return localDAO.findAll();
    }

    // Buscar local por código
    public Local getLocalByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return localDAO.findByCode(code.trim());
    }

    // Buscar locales por nombre
    public List<Local> searchLocalsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllLocals();
        }
        return localDAO.findByName(name.trim());
    }

    // Verificar si un código ya existe
    public boolean isCodeAlreadyExists(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return localDAO.existsByCode(code.trim());
    }

    // Actualizar un local
    public boolean updateLocal(Local local) {
        if (local == null || local.getCodigo() == null) {
            return false;
        }
        return localDAO.update(local);
    }

    // Eliminar un local
    public boolean deleteLocal(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return localDAO.deleteByCode(code.trim());
    }

    // Obtener estadísticas
    public int getTotalLocals() {
        return localDAO.count();
    }

    // Verificar si hay locales registrados
    public boolean hasLocals() {
        return getTotalLocals() > 0;
    }
}
