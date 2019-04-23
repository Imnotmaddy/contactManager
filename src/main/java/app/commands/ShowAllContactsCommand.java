package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowAllContactsCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        List<Contact> contacts = ContactDaoImpl.getInstance().findAll();
        request.setAttribute("contacts", contacts);
        return PagePaths.CONTACT_LIST.getJspPath();
    }
}
