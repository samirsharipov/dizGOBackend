package uz.dizgo.erp.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryTranslateDto {
    private UUID languageId;    // Til ID
    private String name;        // Kategoriya nomi
    private String description; // Kategoriya tavsifi
}