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
public class DismissalDescription extends AbsEntity {

    private String description;

    @ManyToOne
    private Business business;

    //majburiy mi ?
    private boolean mandatory;
}
