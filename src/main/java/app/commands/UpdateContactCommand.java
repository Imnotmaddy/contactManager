package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.ContactService;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactService.getParameters(request);
            contact.setId(Integer.valueOf(request.getParameter("id")));
            contact.setPhoneNumbers(ContactService.getNewNumbers(request, contact.getId()));
            ContactDaoImpl.getInstance().updateContact(contact);
        } catch (AppException e) {

        }
        return new ShowAllContactsCommand().execute(request, response);
    }


}
