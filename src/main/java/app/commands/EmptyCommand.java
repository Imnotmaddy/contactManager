package app.commands;

import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class EmptyCommand implements ActionCommand {
    private static final Logger LOGGER = LogManager.getLogger(EmptyCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Contact> contacts = ContactDaoImpl.getInstance().findAll();
        request.setAttribute("contacts", contacts);
        return "/views/contactList.jsp";
    }
}
