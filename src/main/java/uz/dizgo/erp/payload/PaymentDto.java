package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto{

    private UUID paymentMethodId;

    private Double paidSum;
    private Double paidSumDollar;
    private Boolean isDollar;
}