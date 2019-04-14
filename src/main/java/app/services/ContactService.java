package app.services;

import app.exception.AppException;
import app.models.Contact;
import app.models.PhoneNumber;
import app.sql.dao.mysql.ContactDaoImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Log4j2
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
        byte[] photo = {};
        try {
            String date1 = request.getParameter("date");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(date1);
            sqlStartDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            sqlStartDate = null;
        }
        try {
            Part photoPart = request.getPart("photo");
            photo = IOUtils.toByteArray(photoPart.getInputStream());
            if (photo.length == 0) {
                String oldPhoto = request.getParameter("oldPhoto");
                photo = Base64.getDecoder().decode(oldPhoto.getBytes());
            }

        } catch (IOException | ServletException ex) {
            log.error(ex);
        }

        return new Contact(null, email, name, surname,
                familyName, sqlStartDate, sex, citizenship,
                relationship, webSite, currentJob,
                jobAddress, residenceCountry, residenceCity, residenceStreet, residenceHouseNumber,
                residenceApartmentNumber, index, photo);
        //TODO: add photo
    }

    public static Contact getContactInfoById(Integer id) throws AppException {
        Contact contact = ContactDaoImpl.getInstance().findById(id);
        List<PhoneNumber> numbers = contact.getPhoneNumbers();
        contact.setPhoneNumbers(numbers);
        return contact;
    }
}
