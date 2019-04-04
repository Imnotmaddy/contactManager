package app.commands;

import app.exception.AppException;
import app.sql.dao.mysql.ContactDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddContactCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            UpdateContactCommand contactCommand = new UpdateContactCommand();
            ContactDaoImpl.getInstance().save(contactCommand.getParameters(request));
        } catch (AppException e) {

        }
        return new ShowAllContactsCommand().execute(request, response);
    }
}
