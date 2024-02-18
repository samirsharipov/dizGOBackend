package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LessonGetDto {
    private UUID id;
    private UUID roleId;
    private String roleName;
    private String name;
    private String link;
    private Integer view;
    private UUID attachmentId;
    private String description;
    private boolean hasTest;
}
