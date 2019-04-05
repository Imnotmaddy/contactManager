package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.Entity;

import java.sql.*;
import java.util.Map;
import java.util.function.Function;

abstract class AbstractDaoImpl<T extends Entity> {

    T save(T entity, String sql, Connection connection, Map<Integer, Function<T, Object>> fields) throws SQLException {
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
            return entity;
        }
    }

    T update(T entity, String sql, Connection connection, Map<Integer, Function<T, Object>> fields) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Integer, Function<T, Object>> entry : fields.entrySet()) {
                Integer field = entry.getKey();
                Function<T, Object> value = entry.getValue();
                statement.setObject(field, value.apply(entity));
            }
            statement.setObject(fields.size() + 1, entity.getId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
            }
            return entity;
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
