package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;
import uz.dizgo.erp.enums.ValueType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LidField extends AbsEntity {

    private String name;

    private Boolean tanlangan;

    @Enumerated(EnumType.STRING)
    private ValueType valueType;

    @ManyToOne
    private Business business;

}
