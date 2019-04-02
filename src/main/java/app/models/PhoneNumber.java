package app.models;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumber extends Entity {
    @NonNull
    private String phoneNumber;

    @NonNull
    private String phoneType;

    private String commentary;

    @NonNull
    private Integer contactId;

    @NonNull
    private String countryCode;

    @NonNull
    private String operatorCode;
}
