package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.ContactService;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactDaoImpl.getInstance().save(ContactService.getParameters(request));
            PhoneDaoImpl.getInstance().saveAll(ContactService.getNewNumbers(request, contact.getId()));
        } catch (AppException e) {

        }
        return new ShowAllContactsCommand().execute(request, response);
    }


}
