package uz.pdp.springsecurity.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
