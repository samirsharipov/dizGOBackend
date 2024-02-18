package uz.pdp.springsecurity.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ProjectUser extends AbsEntity {

    @ManyToOne
    User user;

    @ManyToOne
    Project project;
}
