package app.sql.dao;

import app.exception.AppException;
import app.models.Entity;

import java.util.List;

public interface Dao<T extends Entity> {
    T save(T entity) throws AppException;

    List<T> findAll();

    T findById(Integer id);

    void delete(T entity);
}
