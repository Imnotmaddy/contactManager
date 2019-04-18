package app.commands;

import app.exception.AppException;
import app.models.Contact;
import app.services.SearchService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SearchCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Contact> contacts = SearchService.getSearchedUsers(request);
            request.setAttribute("contacts", contacts);
            return PagePaths.CONTACT_LIST.getJspPath();
        } catch (AppException ex) {
            request.setAttribute("error", ex.getMessage());
            return new ShowAllContactsCommand().execute(request, response);
        }
    }
}
