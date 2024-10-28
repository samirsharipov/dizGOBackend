package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Content extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private Double quantity;

    private double cost;

    private boolean costEachOne;

    private double contentPrice;

    private double totalPrice;
}
