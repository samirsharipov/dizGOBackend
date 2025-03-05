package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseOutlay extends AbsEntity {

    @ManyToOne
    private PurchaseOutlayCategory category;

    @ManyToOne
    private Purchase purchase;

    @ManyToOne
    private Business business;

    private double totalPrice;
}
