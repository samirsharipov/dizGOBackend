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
public class LossProduct extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Loss loss;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;


    private double quantity;

    private double price;

    @Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double lastAmount;
    @Column(nullable = false, columnDefinition = "varchar(255) default 'BENEFIT'")
    private String status;

    public LossProduct(Loss loss, double quantity, String status) {
        this.loss = loss;
        this.quantity = quantity;
        this.status = status;
    }
}


