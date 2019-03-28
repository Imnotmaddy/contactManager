package app.commands;

import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ShowAllContactsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Contact> contacts = new ArrayList<>();
        contacts = ContactDaoImpl.getInstance().findAll();
        request.setAttribute("contacts", contacts);
        return "views/contactList.jsp";
    }
}
