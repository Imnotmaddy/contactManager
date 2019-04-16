package app.sql.dao.mysql;

import app.exception.AppException;
import app.models.Attachment;
import app.sql.dao.AttachmentDao;
import app.sql.pool.ConnectionPool;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

@Log4j2
public class AttachmentDaoImpl extends AbstractDaoImpl<Attachment> implements AttachmentDao {

    private AttachmentDaoImpl() {
    }

    private static class AttachmentDaoImplHolder {
        private static final AttachmentDaoImpl INSTANCE = new AttachmentDaoImpl();
    }

    private static final String ATTACHMENTS = "`attachments`";

    private static final Map<Integer, Function<Attachment, Object>> fields;

    private static final String SQL_INSERT_ATTACHMENT = "INSERT INTO " + ATTACHMENTS + "  (`fileName`, `commentary`,`file`, `dateOfCreation`, `contactId`)" +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_DELETE_ATTACHMENT = "DELETE FROM " + ATTACHMENTS + " WHERE `id` = ?";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM" + ATTACHMENTS + " WHERE  `id` = ?";
    private static final String SQL_FIND_ALL_BY_CONTACT_ID = "SELECT *  FROM" + ATTACHMENTS + "WHERE `contactId` = ? ";
    private static final String SQL_UPDATE_ATTACHMENT = "UPDATE" + ATTACHMENTS + " SET `commentary` = ? WHERE `id` = ?";
    private static final String SQL_DELETE_ATTACHMENTS_BY_CONTACT_IDS = "DELETE FROM " + ATTACHMENTS + " WHERE `contactId` in (%s)";
    private static final String SQL_DELETE_ATTACHMENTS_BY_IDS = "DELETE FROM " + ATTACHMENTS + " WHERE `id` in (%s)";

    static {
        fields = new HashMap<>();
        fields.put(1, Attachment::getFileName);
        fields.put(2, Attachment::getCommentary);
        fields.put(3, Attachment::getFile);
        fields.put(4, Attachment::getDateOfCreation);
        fields.put(6, Attachment::getContactId);
    }

    public static AttachmentDaoImpl getInstance() {
        return AttachmentDaoImpl.AttachmentDaoImplHolder.INSTANCE;
    }

    public List<Attachment> saveAll(List<Attachment> attachments) throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            super.saveAll(attachments, SQL_INSERT_ATTACHMENT, fields, connection);
            return attachments;
        } catch (SQLException ex) {
            log.error(ex);
            try {
                rollbackConnection(connection);
            } catch (SQLException e) {
                log.error(e);
            }
            throw new AppException("Error occurred while saving your attachments. Please try again or call our administrator");
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    List<Attachment> findAllByContactId(Integer contactId) throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL_BY_CONTACT_ID)) {
            statement.setInt(1, contactId);
            ResultSet resultSet = statement.executeQuery();
            List<Attachment> attachments = new ArrayList<>();
            while (resultSet.next()) {
                attachments.add(buildAttachment(resultSet));
            }
            return attachments;
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private Attachment buildAttachment(ResultSet resultSet) throws SQLException {
        Attachment attachment = Attachment.builder()
                .commentary(resultSet.getString("commentary"))
                .contactId(resultSet.getInt("contactId"))
                .dateOfCreation(resultSet.getDate("dateOfCreation"))
                .file(resultSet.getBlob("file").getBytes(1, (int) resultSet.getBlob("file").length()))
                .fileName(resultSet.getString("fileName"))
                .build();
        attachment.setId(resultSet.getInt("id"));
        return attachment;
    }

    void deleteByContactIds(Connection connection, Set<Integer> contactIds) throws SQLException {
        super.deleteByGivenIds(connection, contactIds, SQL_DELETE_ATTACHMENTS_BY_CONTACT_IDS);
    }

    public void deleteAllById(Set<Integer> ids) throws AppException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            super.deleteByGivenIds(connection, ids, SQL_DELETE_ATTACHMENTS_BY_IDS);
        } catch (SQLException ex) {
            log.error(ex);
            throw new AppException("An error occurred while deleting attachments. Please, try again in a moment or call our administrator");
        }
    }

    List<Attachment> updateAttachments(List<Attachment> attachments, Connection connection) throws SQLException {
        if (attachments == null) {
            throw new IllegalArgumentException("cannot update Null entities in attachments");
        }
        for (Attachment attachment : attachments) {
            if (attachment.getId() != null) {
                super.persist(attachment, SQL_UPDATE_ATTACHMENT, connection, fields);
            } else {
                super.persist(attachment, SQL_INSERT_ATTACHMENT, connection, fields);
            }
        }
        return attachments;
    }


}
