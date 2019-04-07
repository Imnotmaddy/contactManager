package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowAllContactsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Contact> contacts = ContactDaoImpl.getInstance().findAll();
            request.setAttribute("contacts", contacts);
        } catch (AppException ex) {
            request.setAttribute("error", ex.getMessage());
        }
        return "/views/contactList.jsp";
    }
}
