package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends AbsEntity {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Trade trade;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentMethod payMethod;

    private Double paidSum;
    @Column(columnDefinition = "numeric default 0.0")
    private Double paidSumDollar;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isPayDollar;    
}