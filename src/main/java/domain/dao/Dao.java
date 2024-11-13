package domain.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    boolean add(T entity);
    Optional<T> getById(String id);
    List<T> getAll();
    boolean update(T entity);
    boolean delete(String id);

}
