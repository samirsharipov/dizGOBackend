package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerDebtRepayment extends AbsEntity {

    private Double paidSum;
    @Column(columnDefinition = "numeric default 0.0")
    private Double paidSumDollar;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private PaymentMethod paymentMethod;
private String description;
    private Timestamp payDate;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean isDollarRepayment;

}
