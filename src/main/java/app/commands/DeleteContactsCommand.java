package app.commands;

import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class DeleteContactsCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String[] contacts = request.getParameterMap().get("id");
        Set<Integer> ids = new HashSet<>();
        for (String parameter : contacts) {
            ids.add(Integer.valueOf(parameter));
        }
        ids.forEach(id -> ContactDaoImpl.getInstance().delete(ContactDaoImpl.getInstance().findById(id)));
        return new ShowAllContactsCommand().execute(request, response);
    }
}
