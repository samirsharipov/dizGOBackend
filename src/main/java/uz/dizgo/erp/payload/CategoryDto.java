package uz.dizgo.erp.payload;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryDto {
    private UUID id;

    private String name;

    private String description;

    private UUID parentCategoryId;

    private UUID businessId;

    private List<CategoryTranslateDto> categoryTranslateDtoList;
}
