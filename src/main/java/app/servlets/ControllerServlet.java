package app.servlets;

import app.sql.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add")
public class ControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void init() {
        ConnectionPool.getInstance();
    }

    @Override
    public void destroy() {
        try {
            ConnectionPool.getInstance().destroy();
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
        req.setAttribute("data",strings);
        getServletContext().getRequestDispatcher("/views/add.jsp").forward(req,resp);
    }
}
