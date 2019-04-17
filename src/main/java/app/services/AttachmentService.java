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
            List<Integer> idsForEdit = getAttachmentsIdsForEdit(request);
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
                    if (attachmentBytes.length != 0) {
                        attachments.add(new Attachment(fullFileNames.get(i), fileCommentaries[i], attachmentBytes, dates.get(i), contactId));
                    }
                }
            } catch (IOException ex) {
                log.error(ex);
            }
            if (!idsForEdit.isEmpty()) {
                for (int i = 0; i < idsForEdit.size(); i++) {
                    attachments.get(i).setId(idsForEdit.get(i));
                }
            }
        }
        return attachments;
    }

    private static List<Integer> getAttachmentsIdsForEdit(HttpServletRequest request) {
        String attachments = request.getParameter("attachmentsForEdit");
        if (attachments == null || attachments.isEmpty()) return new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (String el : attachments.split(",")) {
            ids.add(Integer.valueOf(el));
        }
        return ids;
    }
}
