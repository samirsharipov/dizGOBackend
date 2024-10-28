package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.ValueType;

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
