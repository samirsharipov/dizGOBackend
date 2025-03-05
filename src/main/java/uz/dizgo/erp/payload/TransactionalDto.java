package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionalDto {
    private UUID id;
    private UUID customerId; // mijoz
    private UUID branchId; // dukon
    private Double totalAmount; // umumiy savdo summasi
    private Double paidAmount; // chegirma bilan tulangan summa
    private Double discountAmount; // chegirma qilingan summa
}
