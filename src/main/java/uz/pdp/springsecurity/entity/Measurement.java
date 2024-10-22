package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Measurement extends AbsEntity {

    private String name;

    private Double value;


    @ManyToOne
    private Measurement parentMeasurement;

    @ManyToOne
    private Business business;
}
