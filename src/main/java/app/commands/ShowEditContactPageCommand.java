package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.ContactService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class ShowEditContactPageCommand implements ActionCommand {
    private final String EDITCONTACT = "/views/addContact.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Contact contact = ContactService.getContactInfoById(Integer.valueOf(request.getParameter("contactId")));
            request.setAttribute("contact", contact);
            request.setAttribute("phoneNumbers", contact.getPhoneNumbers());
            request.setAttribute("command", "updateContact");

            String photo = Base64.getEncoder().encodeToString(contact.getPhoto());
            request.setAttribute("photo", photo);

        } catch (AppException | IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            request.setAttribute("error", "Unknown error occurred");
        }
        return EDITCONTACT;
    }
}
