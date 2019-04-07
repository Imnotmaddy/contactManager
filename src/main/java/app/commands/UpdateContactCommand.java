package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.ContactService;
import app.services.PhoneService;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactService.getContactParameters(request);
            contact.setId(Integer.valueOf(request.getParameter("id")));
            PhoneDaoImpl.getInstance().deleteAllById(PhoneService.getPhoneNumbersForDelete(request));
            contact.setPhoneNumbers(PhoneService.getAllPhoneNumbers(request, contact.getId()));
            ContactDaoImpl.getInstance().updateContact(contact);
        } catch (AppException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception ex) {
            request.setAttribute("error", "Unknown error occurred");
        }
        return new ShowAllContactsCommand().execute(request, response);
    }


}
