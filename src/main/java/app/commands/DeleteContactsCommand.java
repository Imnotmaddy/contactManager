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
        //TODO: no contact for delete selected!!
        Set<Integer> contactIds = Arrays.stream(request.getParameterMap().get("id"))
                .map(Integer::valueOf).collect(Collectors.toSet());
        ContactDaoImpl.getInstance().deleteAllByIds(contactIds);
        return new ShowAllContactsCommand().execute(request, response);
    }
}
