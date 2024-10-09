package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
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
