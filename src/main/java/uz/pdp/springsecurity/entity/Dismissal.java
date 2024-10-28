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
public class Dismissal extends AbsEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private DismissalDescription description;

    @ManyToOne
    private Business business;

    private String comment;

    private boolean mandatory;
}
