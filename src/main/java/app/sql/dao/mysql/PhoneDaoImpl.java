package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.PhoneNumber;
import app.sql.dao.PhoneDao;
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

public class PhoneDaoImpl extends AbstractDaoImpl<PhoneNumber> implements PhoneDao {

    private static final Logger LOGGER = LogManager.getLogger(PhoneDaoImpl.class);

    private PhoneDaoImpl() {
    }

    private static class PhoneDaoImplHolder {
        private static final PhoneDaoImpl INSTANCE = new PhoneDaoImpl();
    }

    private static final String PHONENUMBERS = "`phone_numbers`";

    private static final Map<Integer, Function<PhoneNumber, Object>> fields;

    private static final String SQL_INSERT_PHONE_NUMBER = "INSERT INTO " + PHONENUMBERS + "  (`phoneNumber`, `phoneType`,`commentary`, `countryCode`, " +
            "`operatorCode`, `contactId`)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PHONE_NUMBER = "DELETE FROM " + PHONENUMBERS + " WHERE `id` = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM" + PHONENUMBERS + " WHERE  `id` = ?";
    private static final String SQL_FIND_ALL_BY_CONTACT_ID = "SELECT *  FROM" + PHONENUMBERS + "WHERE `contactId` = ? ";
    private static final String SQL_UPDATE_PHONE_NUMBER = "UPDATE" + PHONENUMBERS + " SET `phoneNumber` = ?, `phoneType` = ?," +
            "`commentary` = ?, `countryCode` =?,`operatorCode` = ?, `contactId` = ? WHERE `id` = ?";
    private static final String SQL_DELETE_PHONE_NUMBERS_BY_CONTACT_IDS = "DELETE FROM " + PHONENUMBERS + " WHERE `contactId` in (%s)";
    private static final String SQL_DELETE_PHONE_NUMBERS_BY_IDS = "DELETE FROM " + PHONENUMBERS + " WHERE `id` in (%s)";

    static {
        fields = new HashMap<>();
        fields.put(1, PhoneNumber::getPhoneNumber);
        fields.put(2, PhoneNumber::getPhoneType);
        fields.put(3, PhoneNumber::getCommentary);
        fields.put(4, PhoneNumber::getCountryCode);
        fields.put(5, PhoneNumber::getOperatorCode);
        fields.put(6, PhoneNumber::getContactId);
    }

    public static PhoneDaoImpl getInstance() {
        return PhoneDaoImpl.PhoneDaoImplHolder.INSTANCE;
    }

    @Override
    public List<PhoneNumber> saveAll(List<PhoneNumber> phoneNumbers) throws AppException {
        if (phoneNumbers == null) {
            throw new IllegalArgumentException("Can not save null entity!");
        }
        if (phoneNumbers.isEmpty()) return phoneNumbers;

        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            for (PhoneNumber phoneNumber : phoneNumbers) {
                super.persist(phoneNumber, SQL_INSERT_PHONE_NUMBER, connection, fields);
            }
            connection.commit();
            return phoneNumbers;
        } catch (SQLException ex) {
            LOGGER.error(ex);
            try {
                rollbackConnection(connection);
            } catch (SQLException e) {
                LOGGER.error(e);
            }
            throw new AppException("Error occurred while saving your phone numbers. Please try again or call our administrator");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public PhoneNumber findById(Integer id) {
        //TODO: consider deleting this method
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            PhoneNumber number = null;
            if (resultSet.next()) {
                number = buildPhoneNumber(resultSet);
            }
            return number;
        } catch (SQLException e) {
            LOGGER.error(e);
            return null;
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    List<PhoneNumber> findAllByContactId(Integer contactId) throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_CONTACT_ID)) {
            statement.setInt(1, contactId);
            ResultSet resultSet = statement.executeQuery();
            List<PhoneNumber> numbers = new ArrayList<>();
            while (resultSet.next()) {
                numbers.add(buildPhoneNumber(resultSet));
            }
            return numbers;
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private PhoneNumber buildPhoneNumber(ResultSet resultSet) throws SQLException {
        PhoneNumber number = PhoneNumber.builder()
                .phoneNumber(resultSet.getString("phoneNumber"))
                .countryCode(resultSet.getString("countryCode"))
                .operatorCode(resultSet.getString("operatorCode"))
                .commentary(resultSet.getString("commentary"))
                .phoneType(resultSet.getString("phoneType"))
                .contactId(Integer.valueOf(resultSet.getString("contactId")))
                .build();
        number.setId(Integer.valueOf(resultSet.getString("id")));
        return number;
    }

    void deleteByContactIds(Connection connection, Set<Integer> contactIds) throws SQLException {
        String str = contactIds.stream().map(Object::toString).collect(Collectors.joining(", "));
        String query = String.format(SQL_DELETE_PHONE_NUMBERS_BY_CONTACT_IDS, str);
        connection.prepareStatement(query).executeUpdate();
    }

    public void deleteAllById(List<Integer> ids) throws AppException {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        String idArray = ids.stream().map(Object::toString).collect(Collectors.joining(", "));
        String query = String.format(SQL_DELETE_PHONE_NUMBERS_BY_IDS, idArray);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error(ex);
            throw new AppException("An error occurred while deleting phone numbers. Please, try again in a moment or call our administrator");
        }
    }

    List<PhoneNumber> updatePhoneNumbers(List<PhoneNumber> phoneNumbers, Connection connection) throws SQLException {
        List<PhoneNumber> numbers = new ArrayList<>();
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            throw new IllegalArgumentException("cannot update empty entities");
        }

        for (PhoneNumber number : phoneNumbers) {
            if (number.getId() != null) {
                numbers.add(super.persist(number, SQL_UPDATE_PHONE_NUMBER, connection, fields));
            } else {
                numbers.add(super.persist(number, SQL_INSERT_PHONE_NUMBER, connection, fields));
            }
        }
        return numbers;
    }
}
