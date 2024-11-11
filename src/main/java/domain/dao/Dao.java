package domain.dao;

import java.util.List;

public interface Dao<T> {

    boolean add(T entity);
    T getById(String id);
    List<T> getAll();
    boolean update(T entity);
    boolean delete(String id);
    long count();

}
