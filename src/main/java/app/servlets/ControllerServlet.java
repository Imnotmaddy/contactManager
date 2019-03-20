package app.servlets;

import app.models.Contact;
import app.sql.dao.implementation.ContactDaoImpl;
import app.sql.pool.ConnectionPool;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import com.mysql.cj.jdbc.exceptions.ConnectionFeatureNotAvailableException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add")
public class ControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void init() {
        ConnectionPool.getInstance();
       /* Contact contact = new Contact(null, "mail3", "mike", "alifanov",
                "Viktorovich", Date.valueOf(LocalDate.now()), 'F', "Minsk",
                "single", "www.whatever.getofmyhair.com", "unemployed",
                "none", "belarus", "minsk", "street",
                1, 2, 3); */
    }

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
        List<String> strings = new ArrayList<String>();
        strings.add("Mike");
        strings.add("John");
        strings.add("Whatever");
        req.setAttribute("data", strings);
        getServletContext().getRequestDispatcher("/views/addContact.jsp").forward(req, resp);
    }
}
