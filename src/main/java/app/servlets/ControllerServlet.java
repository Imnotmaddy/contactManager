package app.servlets;

import app.commands.ActionCommand;
import app.commands.factory.ActionFactory;
import app.models.Contact;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.pool.ConnectionPool;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/contactManager")
public class ControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroy();
        AbandonedConnectionCleanupThread.checkedShutdown();
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page;
        ActionFactory client = new ActionFactory();
        ActionCommand command;
        command = client.defineCommand(req);
        page = command != null ? command.execute(req, resp) : null;
        RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(page);
        requestDispatcher.forward(req, resp);
        /*
        if (!resp.isCommitted()) {
            if (page != null) {
                RequestDispatcher requestDispatcher = getServletContext().getRequestDispatcher(page);
                requestDispatcher.forward(req, resp);
            } else {
                LOGGER.error("cant process request");
            }

        }*/
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void forwardListContacts(HttpServletRequest req, HttpServletResponse resp, List<Contact> contactList)
            throws ServletException, IOException {
        String nextJSP = "/views/contactList.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("contactList", contactList);
        dispatcher.forward(req, resp);
    }

    private void deleteContact(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("contactId"));
        Contact contact = ContactDaoImpl.getInstance().findById(id);
        ContactDaoImpl.getInstance().delete(contact);

        List<Contact> result = ContactDaoImpl.getInstance().findAll();
        forwardListContacts(req, resp, result);
    }

    private void addContact(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

    }
    /*
    try {
            String name = request.getParameter("name");
            String surname = request.getParameter("surname");
            String familyName = request.getParameter("familyName");
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("date"));
            java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
            String email = request.getParameter("email");
            Contact contact = new Contact(null, email, name, surname,
                    familyName, sqlStartDate, null, null,
                    null, null, null,
                    null, null, null, null, 0
                    , 0, 0);
            try {
                ContactDaoImpl.getInstance().save(contact);
            } catch (AppException e) {

            }
     */

}
