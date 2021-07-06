package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.AttachmentService;
import app.services.ContactService;
import app.services.PhoneService;
import app.sql.dao.mysql.AttachmentDaoImpl;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

@Log4j2
public class UpdateContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        Contact contact = ContactService.getContactParameters(request);
        contact.setId(Integer.valueOf(request.getParameter("id")));
        PhoneDaoImpl.getInstance().deleteAllById(PhoneService.getPhoneNumberIdsForDelete(request));
        AttachmentDaoImpl.getInstance().deleteAllById(
                new HashSet<>(AttachmentService.parseStringForIds(request.getParameter("attachmentsForDelete"))));
        contact.setAttachments(AttachmentService.getAllAttachments(request, contact.getId()));
        contact.setPhoneNumbers(PhoneService.getAllPhoneNumbers(request, contact.getId()));
        ContactDaoImpl.getInstance().updateContact(contact);
        return new ShowAllContactsCommand().execute(request, response);
    }


}