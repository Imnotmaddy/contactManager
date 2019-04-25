package app.scheduler;

import app.exception.AppException;
import app.models.Contact;
import app.services.MailService;
import app.sql.dao.mysql.ContactDaoImpl;
import lombok.extern.log4j.Log4j2;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class emailJob implements Job {
    public void execute(JobExecutionContext context)
            throws JobExecutionException {
//TODO somehow date is not valid for SQL. although the date is fking perfect
        LocalDate date = LocalDate.now();
        date = date.plusDays(1);
        date.format(DateTimeFormatter.ISO_DATE);
        try {
            List<Contact> contacts = ContactDaoImpl.getInstance().getContactsByBirthday(Date.valueOf(date));
            if (contacts == null || contacts.isEmpty()) return;

            Set<String> emails = contacts.stream().map(Contact::getEmail).collect(Collectors.toSet());
            String msgSubject = "Happy Birthday";
            String msgText = "Dear customer, Happy Birthday";
            MailService.sendEmail(emails, msgText, msgSubject);
        } catch (AppException ex) {
            log.error(ex.getMessage());
        }
    }
}
