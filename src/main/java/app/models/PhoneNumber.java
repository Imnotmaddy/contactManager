package app.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class PhoneNumber extends Entity {
    @NonNull
    private String phoneNumber;

    private boolean isCellular; // if false -> the phone is stationary

    private String commentary;

    @NonNull
    private Integer contactId;

    @NonNull
    private String countryCode;

    @NonNull
    private String operatorCode;
}
