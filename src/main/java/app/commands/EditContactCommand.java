package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.models.PhoneNumber;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class EditContactCommand implements ActionCommand {
    private final String EDITCONTACT = "/views/addContact.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Integer id = Integer.valueOf(request.getParameter("contactId"));
            Contact contact = ContactDaoImpl.getInstance().findById(id);
            List<PhoneNumber> numbers = contact.getPhoneNumbers();
            request.setAttribute("contact", contact);
            request.setAttribute("phoneNumbers", numbers);
            request.setAttribute("command", "updateContact");
        } catch (AppException | IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
        } catch (Exception ex) {
            request.setAttribute("error", "Unknown error occurred");
        }
        return EDITCONTACT;
    }
}
