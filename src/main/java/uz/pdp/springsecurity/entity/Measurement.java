package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false AND active = true")
public class Measurement extends AbsEntity {

    private String name;

    private Double value;

    @ManyToOne
    private Measurement parentMeasurement;

    @ManyToOne
    private Business business;
}
