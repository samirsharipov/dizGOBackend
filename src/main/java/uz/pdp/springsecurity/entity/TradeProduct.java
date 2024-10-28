package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.Type;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TradeProduct extends AbsEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trade trade;

    //USE FOR SINGLE TYPE// OR NULL
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @Transient
    private String type;

    private double tradedQuantity;

    private Double backing;

//    private double buyPrice;
    private double totalSalePrice;

    //TOTAL PROFIT OF PRODUCT
    private double profit;

    private Boolean subMeasurement;

    @Transient
    private Double remainQuantity;
}


