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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContactDaoImpl extends AbstractDaoImpl<Contact> implements ContactDao {
    private static final Logger LOGGER = LogManager.getLogger(ContactDaoImpl.class);

    private ContactDaoImpl() {
    }

    private static class ContactDaoImplHolder {
        private static final ContactDaoImpl INSTANCE = new ContactDaoImpl();
    }

    private static final Map<Integer, Function<Contact, Object>> fields;
    private static final String CONTACTS = "`contacts`";
    private static final String SQL_INSERT_CONTACT = "INSERT INTO " + CONTACTS + "  (`email`, `name`,`surname`, `familyName`, " +
            "`dateOfBirth`, `sex`, `citizenship`, `relationShip`, `webSite`, `currentJob`, " +
            "`jobAddress`, `residenceCountry`, `residenceCity`, `residenceStreet`, " +
            "`residenceHouseNumber`, `residenceApartmentNumber`, `index`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_CONTACTS_BY_IDS = "DELETE FROM " + CONTACTS + " WHERE `id` in (%s)";

    private static final String SQL_UPDATE_CONTACT = "UPDATE " + CONTACTS + " SET `email` = ?, `name` = ?, `surname` = ?, " +
            "`familyName` = ?, `dateOfBirth` = ?, `sex` = ?, `citizenship` = ?, `relationShip` = ?, `webSite` = ?, `currentJob` = ?, " +
            "jobAddress = ?, residenceCountry = ?, residenceCity = ?, residenceStreet = ?, " +
            "`residenceHouseNumber` = ?, `residenceApartmentNumber` = ?, `index` = ? WHERE `id` = ?";

    private static final String SQL_FIND_ALL = "SELECT * FROM " + CONTACTS;
    private static final String SQL_FIND_BY_ID = "SELECT * FROM " + CONTACTS + " WHERE `id` = ? ";

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
        if (entity == null) {
            throw new IllegalArgumentException("Can not save null entity!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            return super.persist(entity, SQL_INSERT_CONTACT, connection, fields);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred during contact saving. Please, try submitting your data again.");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public List<Contact> findAll() throws AppException {
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
            throw new AppException("Error occurred during while loading contacts. Please, try again in a minute or call our administrator");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Contact findById(Integer id) throws AppException {
        if (id == null) {
            throw new IllegalArgumentException("Cannot find null entity!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return buildContact(resultSet);
            } else {
                throw new AppException("Contact with id =" + id + " not found. Please, try again or call our administrator");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred while getting the contact. Please, try again or call our administrator");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    public void deleteAllByIds(Set<Integer> ids) throws AppException {
        if (ids == null || ids.isEmpty()) return;
        Connection connection = ConnectionPool.getInstance().getConnection();
        String str = ids.stream().map(Object::toString).collect(Collectors.joining(", "));
        String query = String.format(SQL_DELETE_CONTACTS_BY_IDS, str);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            PhoneDaoImpl.getInstance().deleteByContactIds(connection, ids);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException ex) {
            LOGGER.error(ex);
            try {
                rollbackConnection(connection);
            } catch (SQLException e) {
                LOGGER.error(e);
            }
            throw new AppException("Error occurred during deleting contacts. Please try again in a moment");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }


    @Override
    public Contact updateContact(Contact entity) throws AppException {
        if (entity == null) {
            throw new IllegalArgumentException("Can not update null entity!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            Contact contact = super.persist(entity, SQL_UPDATE_CONTACT, connection, fields);
            contact.setPhoneNumbers(PhoneDaoImpl.getInstance().updatePhoneNumbers(contact.getPhoneNumbers(), connection));
            connection.commit();
            return contact;
        } catch (SQLException e) {
            LOGGER.error(e);
            try {
                rollbackConnection(connection);
            } catch (SQLException ex) {
                LOGGER.error(ex);
            }
            throw new AppException("An error occurred during contact update. Please try again later or call our administrator");
        }
    }

    private Contact buildContact(ResultSet resultSet) throws SQLException {
        Contact contact = Contact.builder()
                .email(resultSet.getString("email"))
                .name(resultSet.getString("name"))
                .surname(resultSet.getString("surname"))
                .familyName(resultSet.getString("familyName"))
                .dateOfBirth(resultSet.getDate("dateOfBirth"))
                .sex(resultSet.getString("sex"))
                .citizenship(resultSet.getString("citizenship"))
                .relationship(resultSet.getString("relationship"))
                .webSite(resultSet.getString("webSite"))
                .currentJob(resultSet.getString("currentJob"))
                .jobAddress(resultSet.getString("jobAddress"))
                .residenceCountry(resultSet.getString("residenceCountry"))
                .residenceCity(resultSet.getString("residenceCity"))
                .residenceStreet(resultSet.getString("residenceStreet"))
                .residenceHouseNumber(resultSet.getString("residenceHouseNumber"))
                .residenceApartmentNumber(resultSet.getString("residenceApartmentNumber"))
                .index(resultSet.getString("index"))
                .build();
        contact.setId(resultSet.getInt("id"));
        contact.setPhoneNumbers(PhoneDaoImpl.getInstance().findAllByContactId(contact.getId()));
        return contact;
    }
}
