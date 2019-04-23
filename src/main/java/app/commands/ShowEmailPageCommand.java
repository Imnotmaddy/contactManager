package app.commands;

import app.exception.AppException;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShowEmailPageCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        String idsForEmail = request.getParameter("idsForEmail");
        if (idsForEmail == null || idsForEmail.isEmpty()) {
            throw new AppException("You didn't set any recipients. Please select contacts via checkboxes and then press button SendEmail");
        }
        Set<Integer> contactIds = new HashSet<>();
        for (String id : idsForEmail.split(",")) {
            contactIds.add(Integer.valueOf(id));
        }
        List<String> recipients = new ArrayList<>();
        ContactDaoImpl.getInstance().findAllByIds(contactIds).forEach(contact -> recipients.add(contact.getEmail()));
        request.setAttribute("recipients", recipients);
        return PagePaths.EMAILS.getJspPath();
    }
}
