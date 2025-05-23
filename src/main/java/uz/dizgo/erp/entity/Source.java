package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Source extends AbsEntity {
    private String name;
    private String icon;
    @ManyToOne
    private Business business;

    public Source(String name, Business business) {
        this.name = name;
        this.business = business;
    }
}
