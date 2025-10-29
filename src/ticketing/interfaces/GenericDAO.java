package ticketing.interfaces;

import java.util.List;

public interface GenericDAO<T> {

    boolean save(T entity);
    T findByCode(String code);
    List<T> findAll();
    boolean update(T entity);
    boolean deleteByCode(String code);
}
