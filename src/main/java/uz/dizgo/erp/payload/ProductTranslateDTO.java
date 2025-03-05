package uz.dizgo.erp.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class ProductTranslateDTO {
    private UUID id;
    private UUID languageId;
    private String languageCode;
    private String languageName;
    private String name;
    private String description;
    private String longDescription;
    private String keywords;
    private String attributes;
}
