package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerCredit extends AbsEntity {

    @ManyToOne(optional = false)
    private Customer customer;

    private LocalDate paymentDate;

    private LocalDate lastPaymentDate;

    @Positive
    private Double amount;

    private String comment;

    @ManyToOne(optional = false)
    private PaymentMethod paymentMethod;

    private Double amountGiven = 0.00;

    private Boolean closedDebt = false;

    private Double totalSumma;
}
