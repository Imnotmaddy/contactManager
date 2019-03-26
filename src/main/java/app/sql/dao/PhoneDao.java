package app.sql.dao;

import app.exception.AppException;
import app.models.PhoneNumber;

import java.util.List;

public interface PhoneDao extends Dao<PhoneNumber> {
    List<PhoneNumber> saveAll(List<PhoneNumber> phoneNumbers) throws AppException;
}
