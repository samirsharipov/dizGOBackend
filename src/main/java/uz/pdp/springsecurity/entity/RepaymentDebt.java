package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RepaymentDebt extends AbsEntity {

    @ManyToOne
    private Customer customer;

    private Double debtSum;
    @Column(columnDefinition = "numeric default 0.0")
    private Double debtSumDollar;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDollarRepayment;

    @ManyToOne
    private PaymentMethod paymentMethod;

    private String description;

    private Boolean delete;

    private Timestamp payDate;
}
