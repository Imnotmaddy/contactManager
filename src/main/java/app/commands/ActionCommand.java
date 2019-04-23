package app.commands;

import app.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ActionCommand {
    String execute(HttpServletRequest request, HttpServletResponse response) throws AppException;


    default void showError(HttpServletRequest request, HttpServletResponse response, String message) {
        try {
            request.getSession().setAttribute("error", message);
            response.sendRedirect(request.getHeader("referer"));
        } catch (IOException ex) {
            //TODO: add smth here
            //what do i do here????
        }
    }
}
