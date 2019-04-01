package app.commands;

import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditContactCommand implements ActionCommand {
    private final String EDITCONTACT = "/views/addContact.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Contact contact;
        Integer id = Integer.valueOf(request.getParameter("contactId"));
        contact = ContactDaoImpl.getInstance().findById(id);
        request.setAttribute("contact", contact);
        request.setAttribute("command", "updateContact");
        return EDITCONTACT;
    }
}
