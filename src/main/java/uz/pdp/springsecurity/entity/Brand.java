package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends AbsEntity {

    private String name;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    public Brand(Business business, String name, boolean active, boolean delete) {
        this.business = business;
        this.name = name;
        super.setActive(active);
        super.setDeleted(delete);
    }
}
