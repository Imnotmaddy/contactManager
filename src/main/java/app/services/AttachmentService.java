package app.services;

import app.models.Attachment;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class AttachmentService {

    public static List<Attachment> getAllAttachments(HttpServletRequest request, Integer contactId) {
        List<Attachment> attachments = new ArrayList<>();
        List<Part> fileParts;
        try {
            fileParts = request.getParts().stream().filter(
                    part -> "attachment".equals(part.getName())).collect(Collectors.toList());
        } catch (ServletException | IOException e) {
            log.error(e);
            return attachments;
        }
        if (!fileParts.isEmpty()) {
            String[] ids = request.getParameterMap().get("attachmentId");
            String[] fileExtensions = request.getParameterMap().get("fileExtension");
            String[] fileNames = request.getParameterMap().get("fileName");
            String[] fileCommentaries = request.getParameterMap().get("fileCommentary");
            String[] datesOfCreation = request.getParameterMap().get("dateOfCreation");

            List<String> fullFileNames = new ArrayList<>();
            for (int i = 0; i < fileNames.length; i++) {
                if (fileNames[i].contains(".")) {
                    fullFileNames.add(fileNames[i]);
                } else {
                    fullFileNames.add(fileNames[i].concat(".").concat(fileExtensions[i]));
                }
            }

            List<Date> dates = new ArrayList<>();
            try {
                for (String s : datesOfCreation) {
                    LocalDate date = LocalDate.parse(s);
                    dates.add(Date.valueOf(date));
                }
            } catch (DateTimeParseException ex) {
                dates.add(null);
            }

            try {
                for (int i = 0; i < fileParts.size(); i++) {
                    Part filePart = fileParts.get(i);
                    byte[] attachmentBytes = IOUtils.toByteArray(filePart.getInputStream());
                    attachments.add(new Attachment(fullFileNames.get(i), fileCommentaries[i], attachmentBytes, dates.get(i), contactId));
                }
            } catch (IOException ex) {
                log.error(ex);
            }
        }
        return attachments;
    }
}
