package app.commands;

import app.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ActionCommand {
    String execute(HttpServletRequest request, HttpServletResponse response) throws AppException;

    default boolean isCommandForwarded(){
        return true;
    }

    default void showError(HttpServletRequest request, HttpServletResponse response, String message) {
        try {
            request.getSession().setAttribute("error", message);
            String header = request.getHeader("referer");
            if (header.contentEquals(request.getRequestURL())){
                response.sendRedirect(request.getRequestURL().toString());
                return;
            }
            response.sendRedirect(header);
        } catch (IOException ex) {
            //TODO: add smth here
            //what do i do here????
        }
    }


}
