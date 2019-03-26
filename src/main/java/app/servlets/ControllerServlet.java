package app.servlets;

import app.exception.AppException;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet("/add")
public class ControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroy();
        AbandonedConnectionCleanupThread.checkedShutdown();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "delete":
                deleteContact(req, resp);
                break;
            case "add":
                addContact(req, resp);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Contact> result = ContactDaoImpl.getInstance().findAll();
        forwardListContacts(req, resp, result);
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
        try {
            String name = req.getParameter("name");
            String surname = req.getParameter("surname");
            String familyName = req.getParameter("familyName");
            java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(req.getParameter("date"));
            java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
            String email = req.getParameter("date");
            Contact contact = new Contact(null, email, name, surname,
                    familyName, sqlStartDate, null, null,
                    null, null, null,
                    null, null, null, null, 0
                    , 0, 0);
            try {
                ContactDaoImpl.getInstance().save(contact);
            } catch (AppException e) {

            }
            List<Contact> result = ContactDaoImpl.getInstance().findAll();
            forwardListContacts(req, resp, result);
        } catch (ParseException e) {
            LOGGER.error(e);
        }
    }

}
