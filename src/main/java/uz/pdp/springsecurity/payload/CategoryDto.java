package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryDto {

    private UUID id;
    @NotNull(message = "required line")
    private String name;
    @NotNull(message = "required line")
    private UUID businessId;
    private String description;
    private UUID parentCategory;

    private String parentCategoryName;

    public CategoryDto(String name, UUID businessId, String description,UUID parentCategory) {
        this.name = name;
        this.businessId = businessId;
        this.description = description;
        this.parentCategory=parentCategory;
    }
}
