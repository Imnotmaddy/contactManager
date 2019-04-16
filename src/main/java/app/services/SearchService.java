package app.services;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j2
public class SearchService {
    public static List<Contact> getSearchedUsers(HttpServletRequest request) throws AppException {
        boolean isQueryEmpty = true;
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String familyName = request.getParameter("familyName");
        String sex = request.getParameter("sex");
        String citizenship = request.getParameter("citizenship");
        String relationship = request.getParameter("relationship");
        String residenceCountry = request.getParameter("residenceCountry");
        String residenceCity = request.getParameter("residenceCity");
        String residenceStreet = request.getParameter("residenceStreet");
        String residenceHouseNumber = request.getParameter("residenceHouseNumber");
        String residenceApartmentNumber = request.getParameter("residenceApartmentNumber");

        java.sql.Date bornAfterDate;
        java.sql.Date bornBeforeDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("dateAfter"));
            bornAfterDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            bornAfterDate = null;
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("dateBefore"));
            bornBeforeDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            bornBeforeDate = null;
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM `contacts` WHERE ");

        if (!name.isEmpty()) {
            sql.append("`name` LIKE ? AND ");
            isQueryEmpty = false;
        }

        if (!surname.isEmpty()) {
            sql.append("`surname` LIKE ? AND ");
            isQueryEmpty = false;
        }
        if (!familyName.isEmpty()) {
            sql.append("`familyName` LIKE ? AND ");
            isQueryEmpty = false;
        }
        if (sex != null) {
            sql.append("`sex` LIKE ? AND ");
            isQueryEmpty = false;
        }
        if (!citizenship.isEmpty()) {
            sql.append("`citizenship` LIKE ? AND ");
            isQueryEmpty = false;
        }

        if (!relationship.isEmpty()) {
            sql.append("`relationship` LIKE ? AND ");
            isQueryEmpty = false;
        }
        if (!residenceCountry.isEmpty()) {
            sql.append("`residenceCountry` LIKE ? AND ");
            isQueryEmpty = false;
        }

        if (!residenceCity.isEmpty()) {
            sql.append("`residenceCity` LIKE ? AND ");
            isQueryEmpty = false;
        }
        if (!residenceStreet.isEmpty()) {
            sql.append("`residenceStreet` LIKE ? AND ");
            isQueryEmpty = false;
        }
        if (!residenceHouseNumber.isEmpty()) {
            sql.append("`residenceHouseNumber` LIKE ? AND ");
            isQueryEmpty = false;
        }

        if (!residenceApartmentNumber.isEmpty()) {
            sql.append("`residenceApartmentNumber` LIKE ? AND ");
            isQueryEmpty = false;
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
            return ContactDaoImpl.getInstance().executeSqlSelect(sql.toString(), name, surname, familyName, sex, citizenship, relationship,
                    residenceCountry, residenceCity, residenceStreet, residenceHouseNumber, residenceApartmentNumber, bornAfterDate, bornBeforeDate);
        } catch (SQLException ex) {
            log.error(ex);
            throw new AppException("Something happened while searching for users. Try again");
        }
    }

}
