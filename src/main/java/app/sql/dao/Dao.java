package app.sql.dao;

import app.exception.AppException;
import app.models.Entity;

public interface Dao<T extends Entity> {
    T findById(Integer id) throws AppException;
}
