package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Warehouse extends AbsEntity {
    //USE FOR SINGLE TYPE
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    private double amount;

    private Date lastSoldDate = new Date();
}
