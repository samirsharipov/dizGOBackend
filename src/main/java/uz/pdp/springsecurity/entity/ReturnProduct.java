package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReturnProduct extends AbsEntity {

    private String invoice;

    private UUID productId;

    private int quantity;

    @ManyToOne
    private Reason reason;

    private String reasonText;

    private double refundAmount;

    private boolean isMonetaryRefund; // true - pul qaytarish, false - almashtirish
}
