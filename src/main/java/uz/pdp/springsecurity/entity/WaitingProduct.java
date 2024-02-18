package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WaitingProduct extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductTypePrice productTypePrice;

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


