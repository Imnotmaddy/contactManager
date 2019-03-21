package app.sql.dao.mysql;

import app.models.Contact;
import app.models.ContactSex;
import app.sql.dao.ContactDao;
import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.Holder;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ContactDaoImpl implements ContactDao {
    private static final Logger LOGGER = LogManager.getLogger(ContactDaoImpl.class);

    private ContactDaoImpl() {
    }

    private static class ContactDaoImplHolder {
        private static final ContactDaoImpl INSTANCE = new ContactDaoImpl();
    }

    private static final String CONTACTS = "`contacts`";
    private static final Map<Long, Function<Contact, Object>> fields;
    private static final String SQL_INSERT_CONTACT = "INSERT INTO " + CONTACTS + "  (`email`, `name`,`surname`, `familyName`, " +
            "`dateOfBirth`, `sex`, `citizenship`, `relationShip`, `webSite`, `currentJob`, " +
            "`jobAddress`, `residenceCountry`, `residenceCity`, `residenceStreet`, " +
            "`residenceHouseNumber`, `residenceApartmentNumber`, `index`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_CONTACT = "DELETE FROM " + CONTACTS + "WHERE `id` = ?";
    private static final String SQL_FIND_ALL = "SELECT *  FROM" + CONTACTS;
    private static final String SQL_FIND_BY_ID = "SELECT *  FROM" + CONTACTS + "WHERE `id` = ? ";

    static {
        fields = new HashMap<>();
        fields.put(0L, Contact::getId);
        fields.put(1L, Contact::getEmail);
        fields.put(2L, Contact::getName);
        fields.put(3L, Contact::getSurname);
        fields.put(4L, Contact::getFamilyName);
        fields.put(5L, Contact::getDateOfBirth);
        fields.put(6L, Contact::getSex);
        fields.put(7L, Contact::getCitizenship);
        fields.put(8L, Contact::getRelationship);
        fields.put(9L, Contact::getWebSite);
        fields.put(10L, Contact::getCurrentJob);
        fields.put(11L, Contact::getJobAddress);
        fields.put(12L, Contact::getResidenceCountry);
        fields.put(13L, Contact::getResidenceCity);
        fields.put(14L, Contact::getResidenceStreet);
        fields.put(15L, Contact::getResidenceHouseNumber);
        fields.put(16L, Contact::getResidenceApartmentNumber);
        fields.put(17L, Contact::getIndex);
    }

    public static ContactDaoImpl getInstance() {
        return ContactDaoImplHolder.INSTANCE;
    }

    @Override
    public boolean save(Contact entity) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_CONTACT, Statement.RETURN_GENERATED_KEYS)
        ) {

            statement.setString(1, entity.getEmail());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getSurname());
            statement.setString(4, entity.getFamilyName());
            statement.setDate(5, entity.getDateOfBirth());
            statement.setString(6, entity.getSex().name());
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
            ResultSet resultSet = statement.getGeneratedKeys();


            if (resultSet.next()) {
                entity.setId(resultSet.getInt(1));
                LOGGER.info("added contact successfully");
                return true;
            }


        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return false;
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
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_DELETE_CONTACT)) {
            statement.setInt(1, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private Contact buildContact(ResultSet resultSet) throws SQLException {
        Contact contact = new Contact();
        contact.setEmail(resultSet.getString("email"));
        contact.setName(resultSet.getString("name"));
        contact.setSurname(resultSet.getString("surname"));
        contact.setFamilyName(resultSet.getString("familyName"));
        contact.setDateOfBirth(resultSet.getDate("dateOfBirth"));
        contact.setSex(ContactSex.valueOf(resultSet.getString("sex")));
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
