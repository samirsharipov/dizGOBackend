package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
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
