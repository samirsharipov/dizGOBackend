package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDto {
    private double balanceSumma;
    private UUID PaymentMethodId;
    private String payMethodName;
    private UUID branchId;
    private String branchName;
}
