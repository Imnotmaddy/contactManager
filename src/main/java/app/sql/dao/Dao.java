package app.sql.dao;

import app.exception.AppException;
import app.models.Entity;

public interface Dao<T extends Entity> {
    T save(T entity) throws AppException;

    T findById(Integer id) throws AppException;

    void delete(T entity) throws AppException;
}
