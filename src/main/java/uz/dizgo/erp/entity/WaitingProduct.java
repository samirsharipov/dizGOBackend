package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaitingProduct extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private double quantity;

    private double totalPrice;

    private double salePrice;

    private Boolean subMeasurement;

    @Transient
    private double amount;

    public WaitingProduct(double quantity, double totalPrice, double salePrice, boolean subMeasurement) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.salePrice = salePrice;
        this.subMeasurement = subMeasurement;
    }
}


