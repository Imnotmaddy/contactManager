package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.ContactDao;
import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ContactDaoImpl extends AbstractDaoImpl<Contact> implements ContactDao {
    private static final Logger LOGGER = LogManager.getLogger(ContactDaoImpl.class);

    private ContactDaoImpl() {
    }

    private static class ContactDaoImplHolder {
        private static final ContactDaoImpl INSTANCE = new ContactDaoImpl();
    }

    private static final String CONTACTS = "`contacts`";
    private static final Map<Integer, Function<Contact, Object>> fields;
    private static final String SQL_INSERT_CONTACT = "INSERT INTO " + CONTACTS + "  (`email`, `name`,`surname`, `familyName`, " +
            "`dateOfBirth`, `sex`, `citizenship`, `relationShip`, `webSite`, `currentJob`, " +
            "`jobAddress`, `residenceCountry`, `residenceCity`, `residenceStreet`, " +
            "`residenceHouseNumber`, `residenceApartmentNumber`, `index`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_CONTACT = "UPDATE " + CONTACTS + " SET `email` = ?, `name` = ?, `surname` = ?, " +
            "`familyName` = ?, `dateOfBirth` = ?, `sex` = ?, `citizenship` = ?, `relationShip` = ?, `webSite` = ?, `currentJob` = ?, " +
            "jobAddress = ?, residenceCountry = ?, residenceCity = ?, residenceStreet = ?, " +
            "`residenceHouseNumber` = ?, `residenceApartmentNumber` = ?, `index` = ? WHERE `id` = ?";

    private static final String SQL_DELETE_CONTACT = "DELETE FROM " + CONTACTS + "WHERE `id` = ?";
    private static final String SQL_FIND_ALL = "SELECT *  FROM" + CONTACTS;
    private static final String SQL_FIND_BY_ID = "SELECT *  FROM" + CONTACTS + "WHERE `id` = ? ";

    static {
        fields = new HashMap<>();
        fields.put(1, Contact::getEmail);
        fields.put(2, Contact::getName);
        fields.put(3, Contact::getSurname);
        fields.put(4, Contact::getFamilyName);
        fields.put(5, Contact::getDateOfBirth);
        fields.put(6, Contact::getSex);
        fields.put(7, Contact::getCitizenship);
        fields.put(8, Contact::getRelationship);
        fields.put(9, Contact::getWebSite);
        fields.put(10, Contact::getCurrentJob);
        fields.put(11, Contact::getJobAddress);
        fields.put(12, Contact::getResidenceCountry);
        fields.put(13, Contact::getResidenceCity);
        fields.put(14, Contact::getResidenceStreet);
        fields.put(15, Contact::getResidenceHouseNumber);
        fields.put(16, Contact::getResidenceApartmentNumber);
        fields.put(17, Contact::getIndex);
    }

    public static ContactDaoImpl getInstance() {
        return ContactDaoImplHolder.INSTANCE;
    }

    @Override
    public Contact save(Contact entity) throws AppException {
        try {
            return super.save(entity, SQL_INSERT_CONTACT, fields);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred during contact saving");
        }
    }

    @Override
    public List<Contact> findAll() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            List<Contact> contacts = new ArrayList<>();
            while (resultSet.next()) {
                contacts.add(buildContact(resultSet));
            }
            return contacts;
        } catch (SQLException e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Contact findById(Integer id) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
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
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public void delete(Contact entity) {
        try {
            super.delete(entity, SQL_DELETE_CONTACT);
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public Contact updateContact(Contact entity) throws AppException {
        try {
            return super.update(entity, SQL_UPDATE_CONTACT, fields);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred during contact update");
        }
    }

    private Contact buildContact(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setId(resultSet.getInt("id"));
        contact.setEmail(resultSet.getString("email"));
        contact.setName(resultSet.getString("name"));
        contact.setSurname(resultSet.getString("surname"));
        contact.setFamilyName(resultSet.getString("familyName"));
        contact.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        contact.setSex(resultSet.getString("sex"));
        contact.setCitizenship(resultSet.getString("citizenship"));
        contact.setRelationship(resultSet.getString("relationship"));
        contact.setWebSite(resultSet.getString("webSite"));
        contact.setCurrentJob(resultSet.getString("currentJob"));
        contact.setJobAddress(resultSet.getString("jobAddress"));
        contact.setResidenceCountry(resultSet.getString("residenceCountry"));
        contact.setResidenceCity(resultSet.getString("residenceCity"));
        contact.setResidenceStreet(resultSet.getString("residenceStreet"));
        contact.setResidenceHouseNumber(resultSet.getString("residenceHouseNumber"));
        contact.setResidenceApartmentNumber(resultSet.getString("residenceApartmentNumber"));
        contact.setIndex(resultSet.getString("index"));
        return contact;
    }
}
