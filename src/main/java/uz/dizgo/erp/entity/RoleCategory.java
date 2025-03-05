package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

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

    @ManyToOne
    private RoleCategory parentRoleCategory;
}
