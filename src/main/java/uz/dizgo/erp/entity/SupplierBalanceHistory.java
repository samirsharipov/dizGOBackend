package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.UUID;

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

    @ElementCollection
    private List<UUID> purchaseIdList;
}
