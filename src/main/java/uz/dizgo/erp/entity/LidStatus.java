package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class LidStatus extends AbsEntity {
    private String name;
    private String color;
    private Integer sort;
    private String orginalName;
    private boolean increase;
    private boolean saleStatus = false;
    @ManyToOne
    private Business business;

    // Yangi konstruktor
    public LidStatus(String name, String color, Integer sort, String orginalName, boolean increase, Business business) {
        this.name = name;
        this.color = color;
        this.sort = sort;
        this.orginalName = orginalName;
        this.increase = increase;
        this.business = business;
    }

    // Konstruktor saleStatus bilan
    public LidStatus(String name, String color, Integer sort, String orginalName, boolean increase, boolean saleStatus, Business business) {
        this(name, color, sort, orginalName, increase, business);
        this.saleStatus = saleStatus;
    }

}