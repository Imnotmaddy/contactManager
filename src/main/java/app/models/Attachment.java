package app.models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment extends Entity {
    @NonNull
    private String fileName;
    private String commentary;
    @NonNull
    private byte[] file;
    @NonNull
    private Date dateOfCreation;
    @NonNull
    private Integer contactId;
}
