package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class Measurement extends AbsEntity {

    private String name; // dona

    private Double value; //10

    @ManyToOne
    private Measurement parentMeasurement; // quti

    @ManyToOne
    private Business business;

    public Measurement(Business business, String name, boolean active, boolean delete) {
        this.business = business;
        this.name = name;
        super.setActive(active);
        super.setDeleted(delete);
    }

    public Measurement(String name, Business business) {
        this.name = name;
        this.business = business;
    }
}