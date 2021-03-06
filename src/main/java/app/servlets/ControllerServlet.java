package app.servlets;

import app.commands.ActionCommand;
import app.commands.factory.ActionFactory;
import app.exception.AppException;
import app.sql.pool.ConnectionPool;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/contactManager")
@MultipartConfig
@Log4j2
public class ControllerServlet extends HttpServlet {

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroy();
        AbandonedConnectionCleanupThread.checkedShutdown();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        ActionFactory client = new ActionFactory();
        ActionCommand command = client.defineCommand(request);
        try {
            page = command.execute(request, response);
            if (command.isCommandForwarded()) {
                getServletContext().getRequestDispatcher(page).forward(request, response);
            }
        } catch (AppException ex) {
            command.showError(request, response, ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            command.showError(request, response, "Unknown error occurred. Please try again");
        }
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

