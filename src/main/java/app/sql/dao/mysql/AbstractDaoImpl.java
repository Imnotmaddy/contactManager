package app.sql.dao.mysql;

import app.models.Entity;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AbstractDaoImpl<T extends Entity> {

    T persist(T entity, String sql, Connection connection, Map<Integer, Function<T, Object>> fields) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (Map.Entry<Integer, Function<T, Object>> entry : fields.entrySet()) {
                Integer field = entry.getKey();
                Function<T, Object> value = entry.getValue();
                statement.setObject(field, value.apply(entity));
            }
            if (entity.getId() != null) {
                statement.setObject(fields.size() + 1, entity.getId());
            }
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
            }
            return entity;
        }
    }

    void deleteById(Integer id, String sql, Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    void rollbackConnection(Connection connection) throws SQLException {
        connection.rollback();
    }

    //TODO: consider adding another ABSTRACT CLASS for PHONE and ATTACHMENTS
    void saveAll(List<T> entities, String sql, Map<Integer, Function<T, Object>> fields, Connection connection) throws SQLException {
        if (entities == null) {
            throw new IllegalArgumentException("Can not save null entities!");
        }
        if (entities.isEmpty()) return;

        connection.setAutoCommit(false);
        for (T entity : entities) {
            persist(entity, sql, connection, fields);
        }
        connection.commit();
    }

    void deleteByGivenIds(Connection connection, Set<Integer> ids, String sql) throws SQLException {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        String str = ids.stream().map(Object::toString).collect(Collectors.joining(", "));
        String query = String.format(sql, str);
        connection.prepareStatement(query).executeUpdate();
    }

}
