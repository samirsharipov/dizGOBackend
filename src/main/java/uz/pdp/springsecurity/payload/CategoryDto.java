package uz.pdp.springsecurity.payload;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryDto {
    private UUID id;

    private String name;

    private UUID parentCategoryId;

    private UUID businessId;

    private List<CategoryTranslateDto> categoryTranslateDtoList;
}
