package app.commands;

import app.exception.AppException;
import app.models.Attachment;
import app.models.Contact;
import app.services.ContactService;
import app.services.PhoneService;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class UpdateContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactService.getContactParameters(request);
            contact.setId(Integer.valueOf(request.getParameter("id")));
            PhoneDaoImpl.getInstance().deleteAllById(PhoneService.getPhoneNumbersForDelete(request));
            byte[] attachment = {};
            String fileName = "";
            List<Attachment> attachments = new ArrayList<>();
            try {
                Part attachmentPart = request.getPart("attachment");
                fileName = attachmentPart.getSubmittedFileName();
                attachment = IOUtils.toByteArray(attachmentPart.getInputStream());
            } catch (IOException | ServletException ex) {
                log.error(ex);
            }
            attachments.add(new Attachment(fileName, "", attachment, contact.getDateOfBirth(), contact.getId()));
            contact.setAttachments(attachments);
            //AttachmentDaoImpl.getInstance().deleteAllById(); TODO: add delete attachments
            contact.setPhoneNumbers(PhoneService.getAllPhoneNumbers(request, contact.getId()));
            ContactDaoImpl.getInstance().updateContact(contact);
        } catch (AppException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception ex) {
            request.setAttribute("error", "Unknown error occurred");
            log.error(ex);
        }
        return new ShowAllContactsCommand().execute(request, response);
    }


}
