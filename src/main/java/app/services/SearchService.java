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
            sql.append("`name` = '").append(name).append("' AND ");
        }

        if (!surname.isEmpty()) {
            sql.append("`surname` = '").append(surname).append("' AND ");
        }
        if (!familyName.isEmpty()) {
            sql.append("`familyName` = '").append(familyName).append("' AND ");
        }
        if (sex != null) {
            sql.append("`sex` = '").append(sex).append("' AND ");
        }
        if (!citizenship.isEmpty()) {
            sql.append("`citizenship` = '").append(citizenship).append("' AND ");
        }

        if (!relationship.isEmpty()) {
            sql.append("`relationship` = '").append(relationship).append("' AND ");
        }
        if (!residenceCountry.isEmpty()) {
            sql.append("`residenceCountry` = '").append(residenceCountry).append("' AND ");
        }

        if (!residenceCity.isEmpty()) {
            sql.append("`residenceCity` = '").append(residenceCity).append("' AND ");
        }
        if (!residenceStreet.isEmpty()) {
            sql.append("`residenceStreet` = '").append(residenceStreet).append("' AND ");
        }
        if (!residenceHouseNumber.isEmpty()) {
            sql.append("`residenceHouseNumber` = '").append(residenceHouseNumber).append("' AND ");
        }

        if (!residenceApartmentNumber.isEmpty()) {
            sql.append("`residenceApartmentNumber` = '").append(residenceApartmentNumber).append("' AND ");
        }

        if (bornAfterDate != null) {
            sql.append("`dateOfBirth` >= ").append(bornAfterDate).append(" AND ");
        }

        if (bornBeforeDate != null) {
            sql.append("`dateOfBirth` <= ").append(bornBeforeDate).append(" AND ");
        }
        sql.delete(sql.length() - 5, sql.length());
        try {
            return ContactDaoImpl.getInstance().executeSqlSelect(sql.toString());
        } catch (SQLException ex) {
            log.error(ex);
            throw new AppException("Something happened while searching for users. Try again");
        }
    }

}
