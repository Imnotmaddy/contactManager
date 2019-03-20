package app.sql.dao.implementation;

import app.models.Contact;
import app.sql.dao.ContactDao;
import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContactDaoImpl implements ContactDao {
    private static final Logger LOGGER = LogManager.getLogger(ContactDaoImpl.class);

    private static final String dbTableName = "`contacts`";

    private static final String SQL_INSERT_CONTACT = "INSERT INTO " + dbTableName + "  (`email`, `name`,`surname`, `familyName`, " +
            "`dateOfBirth`, `sex`, `citizenship`, `relationShip`, `webSite`, `currentJob`, " +
            "`jobAddress`, `residenceCountry`, `residenceCity`, `residenceStreet`, " +
            "`residenceHouseNumber`, `residenceApartmentNumber`, `index`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_CONTACT = "DELETE FROM " + dbTableName + "WHERE `id` = ?";

    private static final String SQL_FIND_ALL = "SELECT *  FROM" + dbTableName;
    private static final String SQL_FIND_BY_ID = "SELECT *  FROM" + dbTableName + "WHERE `id` = ? ";

    @Override
    public boolean save(Contact entity) {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_INSERT_CONTACT)) {
            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getSurname());
            statement.setString(4, entity.getFamilyName());
            statement.setDate(5, entity.getDateOfBirth());
            statement.setString(6, String.valueOf(entity.getSex()));
            statement.setString(7, entity.getCitizenship());
            statement.setString(8, entity.getRelationship());
            statement.setString(9, entity.getWebSite());
            statement.setString(10, entity.getCurrentJob());
            statement.setString(11, entity.getJobAddress());
            statement.setString(12, entity.getResidenceCountry());
            statement.setString(13, entity.getResidenceCity());
            statement.setString(14, entity.getResidenceStreet());
            statement.setInt(15, entity.getResidenceHouseNumber());
            statement.setInt(16, entity.getResidenceApartmentNumber());
            statement.setInt(17, entity.getIndex());
            statement.executeUpdate();
            LOGGER.info("added contact successfully");
            return true;
        } catch (SQLException e) {
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
    public Contact findById(int id) {
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
    public void delete(int id) {
        try (PreparedStatement statement = ConnectionPool.getInstance().getConnection()
                .prepareStatement(SQL_DELETE_CONTACT)) {
            statement.setInt(1, id);
            LOGGER.error(statement.executeUpdate());
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    private Contact buildContact(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setEmail(resultSet.getString("email"));
        contact.setName(resultSet.getString("name"));
        contact.setSurname(resultSet.getString("surname"));
        contact.setFamilyName(resultSet.getString("familyName"));
        contact.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        contact.setSex(resultSet.getString("sex").charAt(0));
        contact.setCitizenship(resultSet.getString("citizenship"));
        contact.setRelationship(resultSet.getString("relationship"));
        contact.setWebSite(resultSet.getString("webSite"));
        contact.setCurrentJob(resultSet.getString("currentJob"));
        contact.setJobAddress(resultSet.getString("jobAddress"));
        contact.setResidenceCountry(resultSet.getString("residenceCountry"));
        contact.setResidenceCity(resultSet.getString("residenceCity"));
        contact.setResidenceStreet(resultSet.getString("residenceStreet"));
        contact.setResidenceHouseNumber(resultSet.getInt("residenceHouseNumber"));
        contact.setResidenceApartmentNumber(resultSet.getInt("residenceApartmentNumber"));
        contact.setIndex(resultSet.getInt("index"));
        return contact;
    }

}
