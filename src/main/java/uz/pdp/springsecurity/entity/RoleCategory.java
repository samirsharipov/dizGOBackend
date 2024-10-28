package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RoleCategory extends AbsEntity {
    private String name;
    private String description;
    @ManyToOne
    private Business business;
}
