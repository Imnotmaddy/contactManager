package app.commands;

import app.exception.AppException;
import app.models.Attachment;
import app.sql.dao.mysql.AttachmentDaoImpl;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = request.getServletContext();
        try {
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

            OutputStream outStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outStream.close();

        } catch (AppException | IOException ex) {
            request.setAttribute("error", ex.getMessage());
        }
        //this return does nothing anyway. there is no redirect after download. it stays on the same page.
        return new ShowAllContactsCommand().execute(request, response);
    }
}
