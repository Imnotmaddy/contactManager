package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
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

            Contact contact = new Contact(null, email, name, surname,
                    familyName, sqlStartDate, sex, citizenship,
                    relationship, webSite, currentJob,
                    jobAddress, residenceCountry, residenceCity, residenceStreet, residenceHouseNumber,
                    residenceApartmentNumber, index);
            ContactDaoImpl.getInstance().save(contact);
        } catch (AppException e) {

        }
        return new ShowAllContactsCommand().execute(request, response);
    }
}
