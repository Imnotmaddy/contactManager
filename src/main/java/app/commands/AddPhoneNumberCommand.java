package app.commands;

import app.exception.AppException;
import app.models.PhoneNumber;
import app.sql.dao.mysql.PhoneDaoImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddPhoneNumberCommand implements ActionCommand {

    private final String EDITCONTACT = "/views/addContact.jsp";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String phoneNumber = request.getParameter("phoneNumber");
        String countryCode = request.getParameter("countryCode");
        String operatorCode = request.getParameter("operatorCode");
        String phoneType = request.getParameter("phoneType");
        String commentary = request.getParameter("commentary");
        Integer contactId = Integer.valueOf(request.getParameter("id"));
        PhoneNumber number = new PhoneNumber(phoneNumber, phoneType, commentary, contactId, countryCode, operatorCode);
        try {
            PhoneDaoImpl.getInstance().save(number);
        } catch (AppException e) {
            System.out.println(e);
        }
        request.setAttribute("command", "updateContact");
        return EDITCONTACT;
    }
}
