package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language extends AbsEntity {
    private String code;         // Til kodi (masalan, 'uz', 'en', 'ru')
    private String name;         // Til nomi
    private String description;  // Tavsif
}
