package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Currency extends AbsEntity {
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;

    @Column(nullable = false)
    private String name = "USD";

    @Column(nullable = false)
    private double course;

    private String description = "DOLLAR";

    private boolean active = true;

    public Currency(Business business, double course) {
        this.business = business;
        this.course = course;
    }
}
