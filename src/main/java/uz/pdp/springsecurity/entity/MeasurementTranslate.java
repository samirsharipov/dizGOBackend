package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementTranslate extends AbsEntity {

    private String name; // Tarjimasi o'lchov nomi

    private String description; // Tarjimasi o'lchov tavsifi

    @ManyToOne(fetch = FetchType.LAZY)
    private Measurement measurement; // O'lchovga bog'langan

    @ManyToOne(fetch = FetchType.LAZY)
    private Language language; // Tarjima tili
}