package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeLidDto {
    private UUID id;
    private Timestamp timestamp;
    private String customerName;
    private String customerPhoneNumber;
    private String branchName;
    private String paymentStatusName;
    private String paymentMethodName;
    private String traderName;
    private double totalSum;
    private double paidSum;
    private double debtSum = 0;
}
