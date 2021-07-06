package app.commands;

import app.exception.AppException;
import app.models.Attachment;
import app.sql.dao.mysql.AttachmentDaoImpl;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Log4j2
public class DownloadCommand implements ActionCommand {


    @Override
    public boolean isCommandForwarded() {
        return false;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        ServletContext context = request.getServletContext();
        Integer attachmentId = Integer.valueOf(request.getParameter("downloadAttachmentId"));
        Attachment attachment = AttachmentDaoImpl.getInstance().findById(attachmentId);
        InputStream inputStream = new ByteArrayInputStream(attachment.getFile());
        String mimeType = context.getMimeType(attachment.getFileName());
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);
        response.setContentLength(attachment.getFile().length);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                attachment.getFileName());
        response.setHeader(headerKey, headerValue);
        try {
            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();
        } catch (IOException ex) {
            log.error("DOWNLOAD COMMAND: " + ex.getMessage());
            throw new AppException("Error while downloading your file. Try again shortly");
        }
        //this return does nothing anyway. there is no redirect after download. it stays on the same page.
        return new ShowAllContactsCommand().execute(request, response);
    }
}
