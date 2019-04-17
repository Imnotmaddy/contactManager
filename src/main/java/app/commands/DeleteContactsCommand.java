package app.commands;

import app.exception.AppException;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DeleteContactsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            //TODO: no contact for delete selected!!
        Set<Integer> contactIds = Arrays.stream(request.getParameterMap().get("id"))
                .map(Integer::valueOf).collect(Collectors.toSet());
            ContactDaoImpl.getInstance().deleteAllByIds(contactIds);
        } catch (AppException | IllegalArgumentException ex) {
            request.setAttribute("error", ex.getMessage());
        } catch (Exception ex){
            request.setAttribute("error", "Unknown error occurred");
        }
        return new ShowAllContactsCommand().execute(request, response);
    }
}
