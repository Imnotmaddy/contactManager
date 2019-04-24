package app.services;

import app.exception.AppException;
import lombok.extern.log4j.Log4j2;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Set;

@Log4j2
public class MailService {
    private final static String HOST = "smtp.gmail.com";
    private final static String USER_NAME = "imnotmaddy123@gmail.com";
    private final static String PASSWORD = "Goddamm1t";
    private final static String PORT = "465";

    public static void sendEmail(Set<String> recipients, String message, String subject) throws AppException {
        //TODO: add templates
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
       /* props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.user", emailFrom);
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        session.setDebug(true);
        msg.setFrom(new InternetAddress(emailFrom));
*/
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(USER_NAME,
                                PASSWORD);
                    }
                });
        MimeMessage msg = new MimeMessage(session);
        try {
            for (String recipient : recipients) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            log.debug("message was probably sent");
        } catch (MessagingException e) {
            log.error(e);
            log.error(session.getDebug());
            throw new AppException("Error during sending your emails");
        }
    }
}


