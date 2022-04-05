package database.dao;

import java.util.List;

public interface DaoI<T> {
    T get(int id);
    List<T> getAll();
    void create(T t);
}
