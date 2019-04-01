package app.sql.dao;

import app.exception.AppException;
import app.models.Contact;

public interface ContactDao extends Dao<Contact> {
    Contact updateContact(Contact entity) throws AppException;
}
