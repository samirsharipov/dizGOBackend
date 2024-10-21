package uz.pdp.springsecurity.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageDto {
    private String code;         // Til kodi (masalan, 'uz', 'en', 'ru')
    private String name;         // Til nomi
    private String description;  //
}
