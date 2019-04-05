package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.Entity;
import app.sql.pool.ConnectionPool;

import java.sql.*;
import java.util.Map;
import java.util.function.Function;

abstract class AbstractDaoImpl<T extends Entity> {

    T save(T entity, String sql, Map<Integer, Function<T, Object>> fields) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("Can not save null entity!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            save(entity, sql, connection, fields);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return entity;
    }

    void save(T entity, String sql, Connection connection, Map<Integer, Function<T, Object>> fields) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Integer, Function<T, Object>> entry : fields.entrySet()) {
                Integer field = entry.getKey();
                Function<T, Object> value = entry.getValue();
                statement.setObject(field, value.apply(entity));
            }
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
            }
        }
    }

    T update(T entity, String sql, Map<Integer, Function<T, Object>> fields) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("Can not update null entity!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            update(entity, sql, connection, fields);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return entity;
    }

    void update(T entity, String sql, Connection connection, Map<Integer, Function<T, Object>> fields) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Integer, Function<T, Object>> entry : fields.entrySet()) {
                Integer field = entry.getKey();
                Function<T, Object> value = entry.getValue();
                statement.setObject(field, value.apply(entity));
            }
            statement.setObject(18, entity.getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
            }
        }
    }

    void delete(Integer id, String sql, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    void rollbackConnection(Connection connection) throws AppException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new AppException("Error during rollback");
        }
    }

}
