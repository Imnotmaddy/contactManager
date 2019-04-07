package app.services;

import app.models.PhoneNumber;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PhoneService {
    public static List<PhoneNumber> getAllPhoneNumbers(HttpServletRequest request, Integer contactId) {
        List<PhoneNumber> numbers = new ArrayList<>();
        String[] ids = request.getParameterMap().get("phoneId");
        String[] phones = request.getParameterMap().get("phoneNumber");
        String[] countryCodes = request.getParameterMap().get("countryCode");
        String[] operatorCodes = request.getParameterMap().get("operatorCode");
        String[] commentaries = request.getParameterMap().get("commentary");
        String[] phoneTypes = request.getParameterMap().get("phoneType");
        if (phones == null) return numbers;
        for (int i = 0; i < phones.length; i++) {
            numbers.add(
                    PhoneNumber.builder()
                            .phoneNumber(phones[i])
                            .phoneType(phoneTypes[i])
                            .commentary(commentaries[i])
                            .contactId(contactId)
                            .countryCode(countryCodes[i])
                            .operatorCode(operatorCodes[i])
                            .build()
            );
        }
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                numbers.get(i).setId(Integer.valueOf(ids[i]));
            }
        }
        return numbers;
    }

    public static List<Integer> getPhoneNumbersForDelete(HttpServletRequest request) {
        String numbers = request.getParameter("numbersForDelete");
        if (numbers == null || numbers.isEmpty()) return new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        for (String el : numbers.split(",")) {
            ids.add(Integer.valueOf(el));
        }
        return ids;
    }
}
