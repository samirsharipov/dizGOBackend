package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerGroup extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double percent;

    @ManyToOne
    private Business business;
}
