package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Bonus extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false)
    private double summa = 0;

    private boolean active = true;

    private boolean delete = false;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Business business;
}
