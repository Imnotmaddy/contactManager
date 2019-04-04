package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.models.PhoneNumber;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class AddContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            UpdateContactCommand contactCommand = new UpdateContactCommand();
            Contact contact = ContactDaoImpl.getInstance().save(contactCommand.getParameters(request));
            PhoneDaoImpl.getInstance().saveAll(getNewNumbers(request, contact.getId()));
        } catch (AppException e) {

        }
        return new ShowAllContactsCommand().execute(request, response);
    }

    List<PhoneNumber> getNewNumbers(HttpServletRequest request, Integer contactId) {
        List<PhoneNumber> numbers = new ArrayList<>();
        String[] phones = request.getParameterMap().get("phoneNumber");
        String[] countryCodes = request.getParameterMap().get("countryCode");
        String[] operatorCodes = request.getParameterMap().get("operatorCode");
        String[] commentaries = request.getParameterMap().get("commentary");
        String[] phoneTypes = request.getParameterMap().get("phoneType");
        for (int i = 0; i < phones.length; i++) {
            numbers.add(buildNumber(phones[i], countryCodes[i], operatorCodes[i], commentaries[i], phoneTypes[i], contactId));
        }
        return numbers;
    }

    PhoneNumber buildNumber(String phoneNumber, String countryCode, String operatorCode, String commentary, String phoneType, Integer contactId) {
        return new PhoneNumber(phoneNumber, phoneType, commentary, contactId, countryCode, operatorCode);
    }
}
