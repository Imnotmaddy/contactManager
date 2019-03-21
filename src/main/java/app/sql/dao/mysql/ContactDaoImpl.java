package app.sql.dao.mysql;

import app.models.Contact;
import app.sql.dao.ContactDao;
import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {
    private static final Logger LOGGER = LogManager.getLogger(ContactDaoImpl.class);

    private static final String dbTableName = "`contacts`";
    private static int columnCount;
    private static List<String> columnNames = new ArrayList<>();

    private static final String SQL_INSERT_CONTACT = "INSERT INTO " + dbTableName + "  (`email`, `name`,`surname`, `familyName`, " +
            "`dateOfBirth`, `sex`, `citizenship`, `relationShip`, `webSite`, `currentJob`, " +
            "`jobAddress`, `residenceCountry`, `residenceCity`, `residenceStreet`, " +
            "`residenceHouseNumber`, `residenceApartmentNumber`, `index`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_CONTACT = "DELETE FROM " + dbTableName + "WHERE `id` = ?";

    private static final String SQL_FIND_ALL = "SELECT *  FROM" + dbTableName;
    private static final String SQL_FIND_BY_ID = "SELECT *  FROM" + dbTableName + "WHERE `id` = ? ";
    private static final String SQL_INIT = "SELECT *  FROM" + dbTableName;

    static {
        init();
    }

    @Override
    public boolean save(Contact entity) {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_INSERT_CONTACT, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 1; i < columnCount; i++) {
                Field field = entity.getClass().getDeclaredField(columnNames.get(i));
                field.setAccessible(true);
                statement.setObject(i, field.get(entity));
            }
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
                LOGGER.info("added contact successfully");
                return true;
            }


        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error(e);
        }
        return false;
    }

    @Override
    public List<Contact> findAll() {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            List<Contact> contacts = new ArrayList<>();
            while (resultSet.next()) {
                contacts.add(buildContact(resultSet));
            }
            return contacts;
        } catch (SQLException e) {
            LOGGER.error(e);
            return new ArrayList<>();
        }
    }

    @Override
    public Contact findById(Integer id) {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            Contact contact = null;
            if (resultSet.next()) {
                contact = buildContact(resultSet);
            }
            return contact;
        } catch (SQLException e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public void delete(Contact entity) {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_DELETE_CONTACT)) {
            statement.setInt(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    private Contact buildContact(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        try {
            for (int i = 1; i < columnCount; i++) {
                Field field = contact.getClass().getDeclaredField(columnNames.get(i));
                field.setAccessible(true);
                field.set(contact, resultSet.getObject(columnNames.get(i)));
            }
            Field field = Contact.class.getSuperclass().getDeclaredField(columnNames.get(0));
            field.setAccessible(true);
            field.set(contact, resultSet.getInt(columnNames.get(0)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.error(e);
        }
        return contact;
    }

    public static void init() {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_INIT)) {
            ResultSet resultSet = statement.executeQuery();
            columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i < columnCount + 1; i++) {
                columnNames.add(resultSet.getMetaData().getColumnName(i));
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        }

    }


}
