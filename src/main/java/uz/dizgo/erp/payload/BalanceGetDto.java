package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceGetDto {
    private double balanceSumma;
    private UUID PaymentMethodId;
    private String payMethodName;
    private String currency;
}
