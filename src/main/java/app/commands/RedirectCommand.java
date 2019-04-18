package app.commands;

import app.services.ContactService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        final String toPage = request.getParameter("page");
        final String error = request.getParameter("error");
        switch (toPage) {
            case "addContact": {
                request.setAttribute("command", "addContact");
                ContactService.addDefaultPhoto(request);
                return PagePaths.ADD_CONTACT.getJspPath();
            }
            case "contactList": {
                if (error != null) {
                    request.setAttribute("error", error);
                }
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
