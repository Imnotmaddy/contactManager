package app.commands;

import app.exception.AppException;
import app.services.MailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SendEmailCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            //TODO: check current recipients for null=> sendback originRecipients
            //TODO: validate all the fields
            String msgText = request.getParameter("msgText");
            String msgSubject = request.getParameter("msgSubject");
            String[] originSetOfRecipients = request.getParameterMap().get("originSetOfRecipients");
            String currentRecipients = request.getParameter("currentRecipients");

            Set<String> recipients = new HashSet<>(Arrays.asList(currentRecipients.split(",")));
            MailService.sendEmail(recipients, msgText, msgSubject);
        } catch (AppException ex) {
            request.setAttribute("error", "Couldnt send emails");
        }

        return new ShowAllContactsCommand().execute(request, response);
    }
}