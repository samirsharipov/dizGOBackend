package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tax extends AbsEntity {

    private String name;

    private Double percent;

    private Boolean active;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

}
