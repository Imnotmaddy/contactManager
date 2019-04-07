package app.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectCommand implements ActionCommand {
    private final static String NEW_CONTACT = "/views/addContact.jsp";
    private final static String PHONE_NUMBER_LIST = "/views/phoneNumberList.jsp";

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
            case "phoneNumberList": {
                return PHONE_NUMBER_LIST;
            }
        }
        return NEW_CONTACT;
    }
}
