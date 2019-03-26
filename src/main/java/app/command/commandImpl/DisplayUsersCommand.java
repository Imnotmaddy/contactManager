package app.command.commandImpl;

import app.command.Action;
import app.command.PagePath;
import app.command.Router;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class DisplayUsersCommand implements Action {
    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router();

        List<Contact> contacts = ContactDaoImpl.getInstance().findAll();
        request.getSession().setAttribute("contactList", contacts);
        router.setPagePath(PagePath.CONTACT_LIST_PAGE.getJspPath());
        router.setRoute(Router.RouteType.REDIRECT);
        return router;
    }
}
