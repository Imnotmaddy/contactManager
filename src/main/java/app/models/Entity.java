package app.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public abstract class Entity {
    @NonNull
    private Integer id;


}
