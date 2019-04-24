package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowAllContactsCommand implements ActionCommand {
    private final static int NUMBER_OF_DISPLAYED_CONTACTS = 10;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        int requestedPage;
        try {
            requestedPage = Integer.parseInt(request.getParameter("requestedPage"));
        } catch (NumberFormatException ex) {
            requestedPage = 1;
        }
        List<Contact> contacts = ContactDaoImpl.getInstance().getContactsWithOffset(
                countOffset(requestedPage),
                NUMBER_OF_DISPLAYED_CONTACTS);
        int numberOfContacts = ContactDaoImpl.getInstance().getTableRowCount();
        int numberOfPages = (numberOfContacts + NUMBER_OF_DISPLAYED_CONTACTS - 1) / NUMBER_OF_DISPLAYED_CONTACTS;

        request.setAttribute("numberOfPages", numberOfPages);
        request.setAttribute("numberOfDisplayedContacts", NUMBER_OF_DISPLAYED_CONTACTS);
        request.setAttribute("contacts", contacts);
        return PagePaths.CONTACT_LIST.getJspPath();
    }

    private Integer countOffset(int requestedPage) {
        return NUMBER_OF_DISPLAYED_CONTACTS * (requestedPage - 1);
    }
}
