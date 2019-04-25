package app.models;

import lombok.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class Contact extends Entity {
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    @NonNull
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    private String familyName;
    private Date dateOfBirth;
    private String sex;
    private String citizenship;
    private String relationship;
    private String webSite;
    private String currentJob;
    private String jobAddress;
    private String residenceCountry;
    private String residenceCity;
    private String residenceStreet;
    private String residenceHouseNumber;
    private String residenceApartmentNumber;
    private String index;
    private byte[] photo;
}
