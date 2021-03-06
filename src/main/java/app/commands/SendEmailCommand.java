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
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        String msgText = request.getParameter("msgText");
        String msgSubject = request.getParameter("msgSubject");
        String currentRecipients = request.getParameter("currentRecipients");

        Set<String> recipients = new HashSet<>(Arrays.asList(currentRecipients.split(",")));
        if (recipients.isEmpty()){
            throw new AppException("Please select recipients via checkboxes, then press send email button");
        }
        MailService.sendEmail(recipients, msgText, msgSubject);
        return new ShowAllContactsCommand().execute(request, response);
    }
}
