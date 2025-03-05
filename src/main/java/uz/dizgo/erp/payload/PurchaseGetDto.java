package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseGetDto {
    private UUID id;
    private Timestamp createdAt;
    private String invoice;
    private String branchName;
    private String supplierName;
    private double debt;
    private String status;
    private double total;
    private String sellerFullName;
}
