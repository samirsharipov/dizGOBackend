package uz.pdp.springsecurity.entity;


import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class Job extends AbsEntity {
    private String name;
    private String description;

    @ManyToOne
    private Business business;
}