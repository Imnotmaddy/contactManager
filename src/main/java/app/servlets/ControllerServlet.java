package app.servlets;

import app.models.Contact;
import app.models.ContactSex;
import app.sql.dao.mysql.ContactDaoImpl;
import app.sql.pool.ConnectionPool;
import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add")
public class ControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void destroy() {
        try {
            ConnectionPool.getInstance().destroy();
            AbandonedConnectionCleanupThread.checkedShutdown();
        } catch (SQLException e) {
            LOGGER.error("failed to destroy connections");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Contact contact = new Contact(null, "mail6", "mike", "alifanov",
                "Viktorovich", Date.valueOf(LocalDate.now()), ContactSex.MALE, "Minsk",
                "single", "www.whatever.getofmyhair.com", "unemployed",
                "none", "belarus", "minsk", "street",
                1, 2, 3);
        System.out.println(ContactDaoImpl.getInstance().findAll().get(0).getSex());
        ContactDaoImpl.getInstance().save(contact);
        System.out.println(ContactDaoImpl.getInstance().findById(48).getSex());
        ContactDaoImpl.getInstance().delete(contact);

        getServletContext().getRequestDispatcher("/views/addContact.jsp").forward(req, resp);
    }
}
