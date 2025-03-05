package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Balance extends AbsEntity {

    private double accountSumma;

    @ManyToOne
    private PaymentMethod paymentMethod;

    @ManyToOne
    private Branch branch;

    private String currency;
}
