package ticketing.local;

import ticketing.utils.ConsoleFormatter;

import java.util.List;

public class LocalController {

    private final LocalDAO localDAO = new LocalDAO();

    public boolean register(Local local) {
        if (local == null || local.getCode() == null || local.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido.");
            return false;
        }

        String normalizedCode = normalizeCode(local.getCode());

        if (localDAO.findByCode(normalizedCode) != null) {
            ConsoleFormatter.printWarning("Ya existe un local con ese código.");
            return false;
        }

        if (local.getName() == null || local.getName().isBlank()) {
            ConsoleFormatter.printError("El nombre del local no puede estar vacío.");
            return false;
        }

        if (local.getCapacity() <= 0) {
            ConsoleFormatter.printError("La capacidad debe ser mayor que cero.");
            return false;
        }

        Local toSave = new Local(
                normalizedCode,
                local.getName().trim(),
                local.getAddress() != null ? local.getAddress().trim() : "",
                local.getCapacity()
        );

        return localDAO.save(toSave);
    }

    public Local findByCode(String code) {
        if (code == null || code.isBlank()) return null;
        return localDAO.findByCode(normalizeCode(code));
    }

    public List<Local> findAll() {
        return localDAO.findAll();
    }

    public boolean update(Local local) {
        if (local == null || local.getCode() == null || local.getCode().isBlank()) {
            ConsoleFormatter.printError("Código inválido para actualizar.");
            return false;
        }

        Local existing = localDAO.findByCode(normalizeCode(local.getCode()));
        if (existing == null) {
            ConsoleFormatter.printWarning("No se encontró un local con ese código.");
            return false;
        }

        Local toUpdate = new Local(
                normalizeCode(local.getCode()),
                local.getName() != null ? local.getName().trim() : existing.getName(),
                local.getAddress() != null ? local.getAddress().trim() : existing.getAddress(),
                local.getCapacity() > 0 ? local.getCapacity() : existing.getCapacity()
        );

        return localDAO.update(toUpdate);
    }

    public boolean delete(String code) {
        if (code == null || code.isBlank()) {
            ConsoleFormatter.printError("Código inválido para eliminar.");
            return false;
        }

        return localDAO.deleteByCode(normalizeCode(code));
    }

    public boolean exists(String code) {
        if (code == null || code.isBlank()) return false;
        return localDAO.findByCode(normalizeCode(code)) != null;
    }

    public boolean hasLocals() {
        List<Local> all = localDAO.findAll();
        return all != null && !all.isEmpty();
    }

    private String normalizeCode(String code) {
        return (code == null) ? null : code.trim().toUpperCase();
    }
}
