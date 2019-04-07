package app.commands;

import app.exception.AppException;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class DeleteContactsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Arrays.stream(request.getParameterMap().get("id"))
                .map(Integer::valueOf)
                .forEach(id -> {
                    try {
                        ContactDaoImpl.getInstance().delete(ContactDaoImpl.getInstance().findById(id));
                    } catch (AppException ex) {

                    }
                });
        return new ShowAllContactsCommand().execute(request, response);
    }
}
