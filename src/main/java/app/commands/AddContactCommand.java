package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.AttachmentService;
import app.services.ContactService;
import app.services.PhoneService;
import app.sql.dao.mysql.AttachmentDaoImpl;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactDaoImpl.getInstance().save(ContactService.getContactParameters(request));
            PhoneDaoImpl.getInstance().saveAll(PhoneService.getAllPhoneNumbers(request, contact.getId()));
            AttachmentDaoImpl.getInstance().saveAll(AttachmentService.getAllAttachments(request, contact.getId()));
        } catch (AppException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception ex) {
            request.setAttribute("error", "Unknown error occurred");

        }
        return new ShowAllContactsCommand().execute(request, response);
    }


}
