package uz.pdp.springsecurity.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CategoryGetDto {
    private UUID id;
    private String name;
    private String description;
    private UUID languageId;
    private String languageCode;
    private UUID parentId;
    private List<CategoryGetDto> children;
}
