package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PurchaseOutlay extends AbsEntity {

    @ManyToOne
    private PurchaseOutlayCategory category;

    @ManyToOne
    private Purchase purchase;

    @ManyToOne
    private Business business;

    private double totalPrice;
}
