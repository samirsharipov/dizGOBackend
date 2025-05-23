package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProduct extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Purchase purchase;
    // USE FOR SINGLE TYPE
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;
    private Double purchasedQuantity;
    private double buyPrice;
    private double salePrice;
    private double totalSum;
}
