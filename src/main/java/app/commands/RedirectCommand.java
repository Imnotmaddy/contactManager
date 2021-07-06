package app.commands;

import app.exception.AppException;
import app.services.ContactService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        final String toPage = request.getParameter("page");
        switch (toPage) {
            case "addContact": {
                request.setAttribute("command", "addContact");
                ContactService.addDefaultPhoto(request);
                return PagePaths.ADD_CONTACT.getJspPath();
            }
            case "contactList": {
                return new ShowAllContactsCommand().execute(request, response);
            }
            case "sendEmail": {
                return PagePaths.EMAILS.getJspPath();
            }
            case "searchPage": {
                return PagePaths.SEARCH_PAGE.getJspPath();
            }
        }
        return PagePaths.ADD_CONTACT.getJspPath();
    }
}
