package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Purchase extends AbsEntity {
    @ManyToOne
    private Supplier supplier;
    @ManyToOne
    private User seller;
    @ManyToOne
    private ExchangeStatus purchaseStatus;
    @ManyToOne
    private PaymentStatus paymentStatus;
    @ManyToOne
    private PaymentMethod paymentMethod;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;
    private Date date;
    private String description;
    private String invoice;
    private double deliveryPrice;
    private double totalSum;
    private double paidSum;
    private double debtSum = 0;
    private boolean editable = true;
}
