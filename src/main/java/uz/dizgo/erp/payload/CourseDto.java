package uz.dizgo.erp.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto{
    @NotNull(message = "REQUIRED")
    private String name;

    private String description;

    private UUID photoId;

    @NotNull(message = "REQUIRED")
    private String link;
}
