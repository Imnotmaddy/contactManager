package app.services;

import app.exception.AppException;
import app.models.Contact;
import app.models.PhoneNumber;
import app.sql.dao.mysql.ContactDaoImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class ContactService {

    public static Contact getContactParameters(HttpServletRequest request) throws AppException{
        String name = request.getParameter("name").trim();
        String surname = request.getParameter("surname").trim();
        String familyName = request.getParameter("familyName").trim();
        String email = request.getParameter("email").trim();
        String sex = request.getParameter("sex").trim();
        String citizenship = request.getParameter("citizenship").trim();
        String relationship = request.getParameter("relationship").trim();
        String webSite = request.getParameter("webSite").trim();
        String currentJob = request.getParameter("currentJob").trim();
        String jobAddress = request.getParameter("jobAddress").trim();
        String residenceCountry = request.getParameter("residenceCountry").trim();
        String residenceCity = request.getParameter("residenceCity").trim();
        String residenceStreet = request.getParameter("residenceStreet").trim();
        String residenceHouseNumber = request.getParameter("residenceHouseNumber").trim();
        String residenceApartmentNumber = request.getParameter("residenceApartmentNumber").trim();
        String index = request.getParameter("index").trim();

        Pattern emailPattern = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
        Matcher matcher = emailPattern.matcher(email);
        if (!matcher.find()){
            throw new AppException("Your email is invalid.");
        }

        Date sqlStartDate;
        try {
            LocalDate date = LocalDate.parse(request.getParameter("date"));
            sqlStartDate = Date.valueOf(date);
        } catch (DateTimeParseException ex) {
            sqlStartDate = null;
        }
        byte[] photo = {};
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

        return new Contact(null, null, email, name, surname,
                familyName, sqlStartDate, sex, citizenship,
                relationship, webSite, currentJob,
                jobAddress, residenceCountry, residenceCity, residenceStreet, residenceHouseNumber,
                residenceApartmentNumber, index, photo);
    }

    public static Contact getContactInfoById(Integer id) throws AppException {
        Contact contact = ContactDaoImpl.getInstance().findById(id);
        List<PhoneNumber> numbers = contact.getPhoneNumbers();
        contact.setPhoneNumbers(numbers);
        return contact;
    }

    public static void addDefaultPhoto(HttpServletRequest request) {
        try {
            byte[] defaultPhoto;
            URL filePath = ContactService.class.getClassLoader().getResource("images/faceless.png");
            if (filePath != null) {
                File file = new File(filePath.getFile());
                defaultPhoto = FileUtils.readFileToByteArray(file);
                request.setAttribute("photo", Base64.getEncoder().encodeToString(defaultPhoto));
            }
        } catch (IOException ex) {
            log.error(ex);
        }
    }

}
