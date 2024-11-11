package uz.pdp.springsecurity.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Where(clause = "active = true")
@Table(indexes = {
        @Index(columnList = "businessId"),
        @Index(columnList = "invoice")
})
public class ReturnProduct extends AbsEntity {

    private String invoice;

    private UUID productId;

    private int quantity;

    private UUID businessId;

    @ManyToOne
    private Reason reason;

    private String reasonText;

    private double refundAmount;

    private boolean isMonetaryRefund; // true - pul qaytarish, false - almashtirish
}
