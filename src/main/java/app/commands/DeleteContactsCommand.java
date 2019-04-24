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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        //TODO IllegalArgumentException
        String[] ids = request.getParameterMap().get("id");
        if (ids == null) {
            throw new AppException("Select contacts for delete via checkboxes, then press button 'Delete'");
        }
        Set<Integer> contactIds = Arrays.stream(ids)
                .map(Integer::valueOf).collect(Collectors.toSet());
        ContactDaoImpl.getInstance().deleteAllByIds(contactIds);
        return new ShowAllContactsCommand().execute(request, response);
    }
}
