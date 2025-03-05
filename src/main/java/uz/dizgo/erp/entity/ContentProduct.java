package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.Transient;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ContentProduct extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Production production;

    //USE FOR SINGLE TYPE// OR NULL
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private double quantity;

    private double totalPrice;

    private boolean byProduct = false;

    private boolean lossProduct = false;

    //get uchun omborda bor maxsulotning amounti
    @Transient
    private double productWarehouseAmount;


}
