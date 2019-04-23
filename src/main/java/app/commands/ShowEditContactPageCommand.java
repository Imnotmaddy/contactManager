package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.ContactService;
import lombok.extern.log4j.Log4j2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Log4j2
public class ShowEditContactPageCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        //TODO: IllegalArgumentException
        Contact contact = ContactService.getContactInfoById(Integer.valueOf(request.getParameter("contactId")));
        request.setAttribute("contact", contact);
        request.setAttribute("phoneNumbers", contact.getPhoneNumbers());
        request.setAttribute("attachments", contact.getAttachments());
        request.setAttribute("command", "updateContact");
        addPhotoToRequest(contact, request);
        return PagePaths.ADD_CONTACT.getJspPath();
    }

    private void addPhotoToRequest(Contact contact, HttpServletRequest request) {
        String photo = Base64.getEncoder().encodeToString(contact.getPhoto());
        if (photo.equals("")) {
            ContactService.addDefaultPhoto(request);
        } else {
            request.setAttribute("photo", photo);
        }
    }
}
