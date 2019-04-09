package app.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectCommand implements ActionCommand {
    private final static String NEW_CONTACT = "/views/addContact.jsp";
    private final static String EMAIL_PAGE = "/views/emails.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        final String toPage = request.getParameter("page");
        switch (toPage) {
            case "addContact": {
                request.setAttribute("command", "addContact");
                return NEW_CONTACT;
            }
            case "contactList": {
                return new ShowAllContactsCommand().execute(request, response);
            }
            case "sendEmail": {
                return EMAIL_PAGE;
            }
        }
        return NEW_CONTACT;
    }
}
