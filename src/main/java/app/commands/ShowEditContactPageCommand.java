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
    private final String EDITCONTACT = "/views/addContact.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactService.getContactInfoById(Integer.valueOf(request.getParameter("contactId")));
            request.setAttribute("contact", contact);
            request.setAttribute("phoneNumbers", contact.getPhoneNumbers());
            request.setAttribute("command", "updateContact");
            addPhotoToRequest(contact, request);
        } catch (AppException | IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            request.setAttribute("error", "Unknown error occurred");
        }
        return EDITCONTACT;
    }

    private void addPhotoToRequest(Contact contact, HttpServletRequest request) throws AppException {
        String photo = Base64.getEncoder().encodeToString(contact.getPhoto());
        if (photo.equals("")) {
            ContactService.addDefaultPhoto(request);
        } else {
            request.setAttribute("photo", photo);
        }
    }
}
