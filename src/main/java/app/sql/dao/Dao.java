package app.sql.dao;

import app.models.Entity;
import app.servlets.ControllerServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends Entity> {
    boolean save(T entity);

    List<T> findAll();

    T findById(int id);

    void delete(int id);
}
