package app.services;

import app.models.PhoneNumber;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                            .phoneNumber(phones[i].trim())
                            .phoneType(phoneTypes[i].trim())
                            .commentary(commentaries[i].trim())
                            .contactId(contactId)
                            .countryCode(countryCodes[i].trim())
                            .operatorCode(operatorCodes[i].trim())
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

    public static Set<Integer> getPhoneNumberIdsForDelete(HttpServletRequest request) {
        String numbers = request.getParameter("numbersForDelete");
        if (numbers == null || numbers.isEmpty()) return new HashSet<>();
        Set<Integer> ids = new HashSet<>();
        for (String el : numbers.split(",")) {
            ids.add(Integer.valueOf(el));
        }
        return ids;
    }
}
