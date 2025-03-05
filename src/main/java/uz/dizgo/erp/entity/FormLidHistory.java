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
public class FormLidHistory extends AbsEntity {
    private double totalSumma;
    private long totalLid;
    private double average;
    private String name;
    private boolean active;
    @ManyToOne
    private Business business;
}
