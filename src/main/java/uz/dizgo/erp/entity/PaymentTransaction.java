package uz.dizgo.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction extends AbsEntity {
    private UUID customerId; // mijoz
    private UUID branchId; // dukon
    private Double totalAmount; // umumiy savdo summasi
    private Double paidAmount; // chegirma bilan tulangan summa
    private Double discountAmount; // chegirma qilingan summa

    private Long transactionId; // plumdan kelishi kerak bolgan id
}
