package app.sql.dao;

import app.exception.AppException;
import app.models.Contact;

import java.util.List;

public interface ContactDao extends Dao<Contact> {
    Contact updateContact(Contact entity) throws AppException;

    List<Contact> findAll() throws AppException;

    Contact save(Contact entity) throws AppException;

    Contact findById(Integer id) throws AppException;
}
