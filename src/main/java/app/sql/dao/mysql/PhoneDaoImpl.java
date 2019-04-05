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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
    private static final String SQL_DELETE_PHONE_NUMBER_BY_CONTACT_ID = "DELETE FROM " + PHONENUMBERS + " WHERE `contactId` = ?";
    private static final String SQL_FIND_ALL = "SELECT *  FROM" + PHONENUMBERS;
    private static final String SQL_FIND_BY_ID = "SELECT * FROM" + PHONENUMBERS + " WHERE  `id` = ?";
    private static final String SQL_FIND_ALL_BY_CONTACT_ID = "SELECT *  FROM" + PHONENUMBERS + "WHERE `contactId` = ? ";
    private static final String SQL_UPDATE_PHONE_NUMBER = "UPDATE" + PHONENUMBERS + " SET `phoneNumber` = ?, `phoneType` = ?," +
            "`commentary` = ?, `countryCode` =?,`operatorCode` = ?, `contactId` = ? WHERE `id` = ?";

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
    public PhoneNumber save(PhoneNumber entity) throws AppException {
        if (entity == null) {
            throw new IllegalArgumentException("Can not save null entity!");
        }
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            return super.save(entity, SQL_INSERT_PHONE_NUMBER, connection, fields);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("Error during phone number save");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public List<PhoneNumber> saveAll(List<PhoneNumber> phoneNumbers) throws AppException {
        if (phoneNumbers.isEmpty()) return phoneNumbers;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            for (PhoneNumber phoneNumber : phoneNumbers) {
                super.save(phoneNumber, SQL_INSERT_PHONE_NUMBER, connection, fields);
            }
            connection.commit();
            return phoneNumbers;
        } catch (SQLException ex) {
            rollbackConnection(connection);
            LOGGER.error(ex);
            throw new AppException("Error during phone number save");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public List<PhoneNumber> findAll() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            List<PhoneNumber> phoneNumbers = new ArrayList<>();
            while (resultSet.next()) {
                phoneNumbers.add(buildPhoneNumber(resultSet));
            }
            return phoneNumbers;
        } catch (SQLException e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public PhoneNumber findById(Integer id) {
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

    public List<PhoneNumber> findAllByContactId(Integer contactId) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_CONTACT_ID)) {
            statement.setInt(1, contactId);
            ResultSet resultSet = statement.executeQuery();
            List<PhoneNumber> numbers = new ArrayList<>();
            while (resultSet.next()) {
                numbers.add(buildPhoneNumber(resultSet));
            }
            return numbers;
        } catch (SQLException e) {
            LOGGER.error(e);
            return new ArrayList<>();
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private PhoneNumber buildPhoneNumber(ResultSet resultSet) throws SQLException {
        PhoneNumber number = new PhoneNumber();
        number.setId(Integer.valueOf(resultSet.getString("id")));
        number.setCommentary(resultSet.getString("commentary"));
        number.setCountryCode(resultSet.getString("countryCode"));
        number.setPhoneNumber(resultSet.getString("phoneNumber"));
        number.setPhoneType(resultSet.getString("phoneType"));
        number.setOperatorCode(resultSet.getString("operatorCode"));
        number.setContactId(Integer.valueOf(resultSet.getString("contactId")));
        return number;
    }

    @Override
    public void delete(PhoneNumber entity) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            super.delete(entity.getId(), SQL_DELETE_PHONE_NUMBER, connection);
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    void deleteAllbyContactId(List<PhoneNumber> phoneNumbers, Connection connection, Integer contactId) throws AppException {
        try {
            for (int i = 0; i < phoneNumbers.size(); i++) {
                super.delete(phoneNumbers.get(i).getContactId(), SQL_DELETE_PHONE_NUMBER_BY_CONTACT_ID, connection);
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("Error during deleting phones");
        }
    }

    List<PhoneNumber> updatePhoneNumbersByContactId(List<PhoneNumber> phoneNumbers, Connection connection) throws AppException {
        List<PhoneNumber> numbers = new ArrayList<>();
        try {
            for (int i = 0; i < phoneNumbers.size(); i++) {
                PhoneNumber number = phoneNumbers.get(i);
                if (number.getId() != null) {
                    numbers.add(super.update(phoneNumbers.get(i), SQL_UPDATE_PHONE_NUMBER, connection, fields));
                } else {
                    numbers.add(super.save(number, SQL_INSERT_PHONE_NUMBER, connection, fields));
                }
            }
            return numbers;
        } catch (SQLException ex) {
            LOGGER.error(ex);
            throw new AppException("Error during Update PhoneNUmbers");
        }
    }
}
