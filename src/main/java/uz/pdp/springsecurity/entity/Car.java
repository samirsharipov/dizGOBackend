package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car extends AbsEntity {
    private String driver;
    private String model;
    private String color;
    private String carNumber;
    private boolean active;
    @ManyToOne
    private Business business;
    @Column(columnDefinition = "numeric default 0.0")
    private Double price;
    @OneToOne
    private Attachment file;
}
