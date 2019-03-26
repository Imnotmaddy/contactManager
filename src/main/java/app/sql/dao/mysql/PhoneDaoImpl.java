package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.PhoneNumber;
import app.sql.dao.PhoneDao;
import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
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

    private static final String SQL_INSERT_PHONE_NUMBER = "INSERT INTO " + PHONENUMBERS + "  (`phoneNumber`, `isCellular`,`commentary`, `countryCode`, " +
            "`operatorCode`, `contactId`)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_PHONE_NUMBER = "DELETE FROM " + PHONENUMBERS + "WHERE `id` = ?";
    private static final String SQL_FIND_ALL = "SELECT *  FROM" + PHONENUMBERS;
    private static final String SQL_FIND_BY_ID = "SELECT *  FROM" + PHONENUMBERS + "WHERE `id` = ? ";

    static {
        fields = new HashMap<>();
        fields.put(0, PhoneNumber::getId);
        fields.put(1, PhoneNumber::getPhoneNumber);
        fields.put(2, PhoneNumber::isCellular);
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
        try {
            return super.save(entity, SQL_INSERT_PHONE_NUMBER, fields);
        } catch (SQLException e) {
            LOGGER.error(e);
            throw new AppException("Error during phone number save");
        }
    }

    @Override
    public List<PhoneNumber> saveAll(List<PhoneNumber> phoneNumbers) throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            for (PhoneNumber phoneNumber : phoneNumbers) {
                save(phoneNumber, SQL_INSERT_PHONE_NUMBER, connection, fields);
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
        return null;
    }

    @Override
    public PhoneNumber findById(Integer id) {
        return null;
    }

    @Override
    public void delete(PhoneNumber entity) {

    }
}
