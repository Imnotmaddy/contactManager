package app.services;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
public class SearchService {
    private static Map<String, String> fields = new HashMap<>();

    static {
        fields.put("name", "");
        fields.put("surname", "");
        fields.put("familyName", "");
        fields.put("sex", "");
        fields.put("citizenship", "");
        fields.put("relationship", "");
        fields.put("residenceCountry", "");
        fields.put("residenceCity", "");
        fields.put("residenceStreet", "");
        fields.put("residenceHouseNumber", "");
        fields.put("residenceApartmentNumber", "");
    }

    public static List<Contact> getSearchedUsers(HttpServletRequest request) throws AppException {
        boolean isQueryEmpty = true;
        Map<String, String> searchFields = new HashMap<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM `contacts` WHERE ");
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            entry.setValue(request.getParameter(entry.getKey()));
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                sql.append("`").append(entry.getKey()).append("` LIKE ? AND ");
                isQueryEmpty = false;
                searchFields.put(entry.getKey(), entry.getValue().trim());
            }
        }

        java.sql.Date bornAfterDate;
        java.sql.Date bornBeforeDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("bornAfterDate"));
            bornAfterDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            bornAfterDate = null;
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("bornBeforeDate"));
            bornBeforeDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            bornBeforeDate = null;
        }

        if (bornAfterDate != null) {
            sql.append("`dateOfBirth` > '").append(bornAfterDate).append("' AND ");
            isQueryEmpty = false;
        }

        if (bornBeforeDate != null) {
            sql.append("`dateOfBirth` < '").append(bornBeforeDate).append("' AND ");
            isQueryEmpty = false;
        }

        if (isQueryEmpty) {
            return ContactDaoImpl.getInstance().findAll();
        }

        sql.delete(sql.length() - " AND ".length(), sql.length()); // deleting " AND "

        try {
            return ContactDaoImpl.getInstance().executeSqlSelect(sql.toString(), searchFields, bornAfterDate, bornBeforeDate);
        } catch (SQLException ex) {
            log.error(ex);
            throw new AppException("Something happened while searching for users. Try again");
        }
    }

}
