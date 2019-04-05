package app.services;

import app.models.Contact;
import app.models.PhoneNumber;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactService {
    public static List<PhoneNumber> getNewNumbers(HttpServletRequest request, Integer contactId) {
        List<PhoneNumber> numbers = new ArrayList<>();
        String[] existingPhonesId = request.getParameterMap().get("phoneId");
        String[] phones = request.getParameterMap().get("phoneNumber");
        String[] countryCodes = request.getParameterMap().get("countryCode");
        String[] operatorCodes = request.getParameterMap().get("operatorCode");
        String[] commentaries = request.getParameterMap().get("commentary");
        String[] phoneTypes = request.getParameterMap().get("phoneType");
        if (phones == null) return numbers;
        for (int i = 0; i < phones.length; i++) {
            numbers.add(buildNumber(phones[i], countryCodes[i], operatorCodes[i], commentaries[i], phoneTypes[i], contactId));
        }
        if (existingPhonesId != null) {
            for (int i = 0; i < existingPhonesId.length; i++) {
                numbers.get(i).setId(Integer.valueOf(existingPhonesId[i]));
            }
        }
        return numbers;
    }

    public static List<Integer> getNumbersForDelete(HttpServletRequest request) {
        String numbers = request.getParameter("numbersForDelete");
        if (numbers == "") return new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (String el : numbers.split(",")) {
            ids.add(Integer.valueOf(el));
        }
        return ids;
    }

    static PhoneNumber buildNumber(String phoneNumber, String countryCode, String operatorCode, String commentary, String phoneType, Integer contactId) {
        return new PhoneNumber(phoneNumber, phoneType, commentary, contactId, countryCode, operatorCode);
    }

    public static Contact getParameters(HttpServletRequest request) {
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
