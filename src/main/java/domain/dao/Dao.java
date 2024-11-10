package domain.dao;

import java.util.List;

public interface Dao<T> {

    boolean add(T entity);
    T getById(int id);
    List<T> getAll();
    boolean update(T entity);
    boolean delete(int id);
    long count();

}
