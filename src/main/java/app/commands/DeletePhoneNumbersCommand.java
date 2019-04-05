package app.commands;

import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

public class DeletePhoneNumbersCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String[] numbers = request.getParameterMap().get("phoneIdForDelete");
        Set<Integer> ids = new HashSet<>();
        for (String parameter : numbers) {
            ids.add(Integer.valueOf(parameter));
        }
        ids.forEach(id -> PhoneDaoImpl.getInstance().delete(PhoneDaoImpl.getInstance().findById(id)));
        return new ShowAllContactsCommand().execute(request, response);
    }
}

