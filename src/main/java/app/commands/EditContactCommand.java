package app.commands;

import app.models.Contact;
import app.models.PhoneNumber;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class EditContactCommand implements ActionCommand {
    private final String EDITCONTACT = "/views/addContact.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Contact contact;
        Integer id = Integer.valueOf(request.getParameter("contactId"));
        contact = ContactDaoImpl.getInstance().findById(id);
        List<PhoneNumber> numbers = PhoneDaoImpl.getInstance().findAllByContactId(contact.getId());
        request.setAttribute("contact", contact);
        request.setAttribute("phoneNumbers", numbers);
        request.setAttribute("command", "updateContact");
        return EDITCONTACT;
    }
}
