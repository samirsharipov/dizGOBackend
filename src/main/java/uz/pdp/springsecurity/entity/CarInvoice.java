package uz.pdp.springsecurity.entity;

import lombok.*;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.CarInvoiceType;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "deleted = false AND active = true")
public class CarInvoice extends AbsEntity {
    @Enumerated(EnumType.STRING)
    private CarInvoiceType type;
    private Double amount;
    @ManyToOne
    private Car car;
    @ManyToOne
    private Branch branch;
    @Column(length = 10000)
    private String description;
    @ManyToOne
    private PaymentMethod paymentMethod;
    private String mileage;
}
