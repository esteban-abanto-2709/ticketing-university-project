package ticketing.dao.temporary;

import java.util.ArrayList;
import java.util.List;
import ticketing.dao.interfaces.LocalDAO;
import ticketing.entities.Local;

public class LocalDAOTemporary implements LocalDAO {

    private static List<Local> locales = new ArrayList<>();

    @Override
    public boolean save(Local local) {
        // Verificar que el código no exista
        if (existsByCode(local.getCodigo())) {
            return false;
        }
        return locales.add(local);
    }

    @Override
    public List<Local> findAll() {
        // Retorna copia para evitar modificaciones
        return new ArrayList<>(locales);
    }

    @Override
    public Local findByCode(String code) {
        for (Local local : locales) {
            if (local.getCodigo().equalsIgnoreCase(code)) {
                return local;
            }
        }
        return null;
    }

    @Override
    public List<Local> findByName(String name) {
        List<Local> result = new ArrayList<>();
        String searchName = name.toLowerCase();

        for (Local local : locales) {
            if (local.getNombre().toLowerCase().contains(searchName)) {
                result.add(local);
            }
        }
        return result;
    }

    @Override
    public boolean existsByCode(String code) {
        return findByCode(code) != null;
    }

    @Override
    public boolean update(Local local) {
        for (int i = 0; i < locales.size(); i++) {
            if (locales.get(i).getCodigo().equalsIgnoreCase(local.getCodigo())) {
                locales.set(i, local);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteByCode(String code) {
        return locales.removeIf(local -> local.getCodigo().equalsIgnoreCase(code));
    }

    @Override
    public int count() {
        return locales.size();
    }
}
