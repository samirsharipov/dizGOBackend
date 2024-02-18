package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonDto {
    @NotNull
    private String name;

    @NotNull
    private UUID roleId;

    @NotNull
    private String link;

    @NotNull
    private Integer view;

    private UUID attachmentId;

    private String description;
}
