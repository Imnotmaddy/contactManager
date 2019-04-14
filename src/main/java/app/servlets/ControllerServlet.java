package app.servlets;

import app.commands.ActionCommand;
import app.commands.factory.ActionFactory;
import app.sql.pool.ConnectionPool;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/contactManager")
@MultipartConfig(maxFileSize = 1024*1024*5)
public class ControllerServlet extends HttpServlet {

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
        getServletContext().getRequestDispatcher(page).forward(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}

