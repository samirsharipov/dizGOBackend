package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductTypeCombo extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Product mainProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    private Product contentProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    private ProductTypePrice contentProductTypePrice;

    private double amount;

    private double buyPrice;

    private double salePrice;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Measurement measurement;
}
