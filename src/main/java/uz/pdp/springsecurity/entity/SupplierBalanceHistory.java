package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SupplierBalanceHistory extends AbsEntity {
    private Double amount;
    @ManyToOne
    private PaymentMethod paymentMethod;
    @ManyToOne
    private Supplier supplier;

    private String description;
    @ManyToOne
    private User user;
    @ManyToOne
    private Branch branch;
}
