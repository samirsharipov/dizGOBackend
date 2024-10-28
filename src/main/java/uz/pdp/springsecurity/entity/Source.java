package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Source extends AbsEntity {
    private String name; //qolda kiritish125445625624
    private String icon;
    @ManyToOne
    private Business business;
}
