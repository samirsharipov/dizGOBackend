package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class Language extends AbsEntity {
    private String code;         // Til kodi (masalan, 'uz', 'en', 'ru')
    private String name;         // Til nomi
    private String description;  // Tavsif
}
