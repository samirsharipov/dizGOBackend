package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.CarInvoiceType;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
