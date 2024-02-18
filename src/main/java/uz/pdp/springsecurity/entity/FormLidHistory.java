package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
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
