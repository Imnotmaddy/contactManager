package app.services;

import app.models.Contact;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactService {

    public static Contact getContactParameters(HttpServletRequest request) {
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String familyName = request.getParameter("familyName");
        String email = request.getParameter("email");
        String sex = request.getParameter("sex");
        String citizenship = request.getParameter("citizenship");
        String relationship = request.getParameter("relationship");
        String webSite = request.getParameter("webSite");
        String currentJob = request.getParameter("currentJob");
        String jobAddress = request.getParameter("jobAddress");
        String residenceCountry = request.getParameter("residenceCountry");
        String residenceCity = request.getParameter("residenceCity");
        String residenceStreet = request.getParameter("residenceStreet");
        String residenceHouseNumber = request.getParameter("residenceHouseNumber");
        String residenceApartmentNumber = request.getParameter("residenceApartmentNumber");
        String index = request.getParameter("index");
        java.sql.Date sqlStartDate;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date"));
            sqlStartDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            sqlStartDate = null;
        }

        return new Contact(null, email, name, surname,
                familyName, sqlStartDate, sex, citizenship,
                relationship, webSite, currentJob,
                jobAddress, residenceCountry, residenceCity, residenceStreet, residenceHouseNumber,
                residenceApartmentNumber, index);
    }
}
