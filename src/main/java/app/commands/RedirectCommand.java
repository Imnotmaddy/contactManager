package app.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirectCommand implements ActionCommand {
    private final String NEWCONTACT = "/views/addContact.jsp";
    private final String PHONENUMBERLIST = "/views/phoneNumberList.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        final String toPage = request.getParameter("page");
        switch (toPage) {
            case "addContact": {
                request.setAttribute("command", "addContact");
                return NEWCONTACT;
            }
            case "contactList": {
                return new ShowAllContactsCommand().execute(request, response);
            }
            case "phoneNumberList": {
                return PHONENUMBERLIST;
            }
        }
        return NEWCONTACT;
    }
}
