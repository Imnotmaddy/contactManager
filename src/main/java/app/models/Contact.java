package app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.time.LocalDateTime;

//TODO rename Contact->User ?
@Data
@EqualsAndHashCode(callSuper = false)
public class Contact extends Entity {
    @NonNull private String telephoneNumber;

    //TODO mobile phone number or stationary phone?

    private String email;
    @NonNull private String name;
    @NonNull private String surname;
    private String familyName;
    private LocalDateTime dateOfBirth;
    private boolean isMale;
    private String citizenship;
    private String relationship;
    private String webSite;
    private String currentJob;
    private String jobAdress;
    private String residenceCountry;
    private String residenceCity;
    private String street;
    private int residenceHouseNumber;
    private int residenceApartmentNumber;
    private int index;
}
