package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Trade extends AbsEntity {

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User trader;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Branch branch;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PaymentMethod payMethod;

    private Date payDate;

    private double totalSum = 0;
    private double totalSumDollar = 0;

    private double paidSum;
    private double paidSumDollar;

    private double debtSum = 0;
    private double debtSumDollar = 0;

    private double totalProfit = 0.0;

    private boolean editable = true;

    @ManyToOne(cascade = CascadeType.ALL)
    private Address address;

    private boolean lid ;

    private String dollar;

    private String gross;

    private Double kpi;

    private String invoice;

    private Boolean backing;

    private boolean dollarTrade;

    private String tradeDescription;

    private Boolean differentPayment;
}