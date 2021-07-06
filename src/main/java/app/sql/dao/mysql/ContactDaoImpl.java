package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.ContactDao;
import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.sql.Date;
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
            "`residenceHouseNumber`, `residenceApartmentNumber`, `index`, `photo`)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_DELETE_CONTACTS_BY_IDS = "DELETE FROM " + CONTACTS + " WHERE `id` in (%s)";
    private static final String SQL_FIND_CONTACTS_BY_IDS = "SELECT * FROM " + CONTACTS + " WHERE `id` in (%s)";

    private static final String SQL_UPDATE_CONTACT = "UPDATE " + CONTACTS + " SET `email` = ?, `name` = ?, `surname` = ?, " +
            "`familyName` = ?, `dateOfBirth` = ?, `sex` = ?, `citizenship` = ?, `relationShip` = ?, `webSite` = ?, `currentJob` = ?, " +
            "jobAddress = ?, residenceCountry = ?, residenceCity = ?, residenceStreet = ?, " +
            "`residenceHouseNumber` = ?, `residenceApartmentNumber` = ?, `index` = ?, `photo` = ? WHERE `id` = ?";

    private static final String SQL_FIND_ALL = "SELECT * FROM " + CONTACTS;
    private static final String SQL_FIND_BY_ID = "SELECT * FROM " + CONTACTS + " WHERE `id` = ? ";
    private static final String SQL_FIND_WITH_OFFSET = "SELECT * FROM " + CONTACTS + " limit ? offset ?";
    private static final String SQL_FIND_BY_BIRTHDAY = "SELECT * FROM " + CONTACTS + "WHERE MONTH(`dateOfBirth`) = MONTH(?) AND DAY(`dateOfBirth`) = DAY(?)";

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
        fields.put(18, Contact::getPhoto);
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
        } catch (SQLIntegrityConstraintViolationException ex) {
            LOGGER.error(ex);
            throw new AppException("Email that you submitted is already in use. Try another one");
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred during contact saving. Please, try submitting your data again.");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    public List<Contact> findAllByIds(Set<Integer> ids) throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        String str = ids.stream().map(Object::toString).collect(Collectors.joining(", "));
        String query = String.format(SQL_FIND_CONTACTS_BY_IDS, str);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            List<Contact> contacts = new ArrayList<>();
            while (resultSet.next()) {
                contacts.add(buildContact(resultSet));
            }
            return contacts;
        } catch (SQLException ex) {
            LOGGER.error(ex);
            throw new AppException("Could not get selected contacts info, try again later.");
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
            AttachmentDaoImpl.getInstance().deleteByContactIds(connection, ids);
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
            contact.setAttachments(AttachmentDaoImpl.getInstance().updateAttachments(contact.getAttachments(), connection));
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
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
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
        Blob photoBlob = resultSet.getBlob("photo");
        contact.setPhoto(photoBlob.getBytes(1, (int) photoBlob.length()));
        contact.setId(resultSet.getInt("id"));
        contact.setPhoneNumbers(PhoneDaoImpl.getInstance().findAllByContactId(contact.getId()));
        contact.setAttachments(AttachmentDaoImpl.getInstance().findAllByContactId(contact.getId()));
        return contact;
    }

    public List<Contact> executeSqlSelect(String sql, Map<String, String> searchFields, Date bornAfterDate, Date bornBeforeDate) throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            buildStatementForSearch(statement, sql, searchFields, bornAfterDate, bornBeforeDate);

            ResultSet resultSet = statement.executeQuery();
            List<Contact> contacts = new ArrayList<>();
            while (resultSet.next()) {
                contacts.add(buildContact(resultSet));
            }
            return contacts;
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private void buildStatementForSearch(PreparedStatement statement, String query, Map<String, String> searchFields,
                                         Date bornAfterDate, Date bornBeforeDate) throws SQLException {
        int index = 1;

        for (Map.Entry<String, String> entry : searchFields.entrySet()) {
            statement.setString(index, "%" + entry.getValue()+ "%");
            index++;
        }

        if (query.contains("`bornAfterDate`")) {
            statement.setDate(index, bornAfterDate);
            index++;
        }

        if (query.contains("`bornBeforeDate`")) {
            statement.setDate(index, bornBeforeDate);
        }
    }

    public List<Contact> getContactsWithOffset(int offset, int limit) throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_WITH_OFFSET)) {
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet resultSet = statement.executeQuery();
            List<Contact> contacts = new ArrayList<>();
            while (resultSet.next()) {
                contacts.add(buildContact(resultSet));
            }
            return contacts;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred while retrieving contacts");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    public int getTableRowCount() throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM contacts")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("An error occurred while retrieving contacts");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    public List<Contact> getContactsByBirthday(Date date) throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_BIRTHDAY)) {
            statement.setDate(1, date);
            statement.setDate(2, date);
            ResultSet resultSet = statement.executeQuery();
            List<Contact> contacts = new ArrayList<>();
            while (resultSet.next()) {
                contacts.add(buildContact(resultSet));
            }
            return contacts;
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("Could not retrieve contacts");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
}
