package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCreditGetDto {
    private UUID id;
    private UUID customerId;
    private String customerNumber;
    private String name;
    private LocalDate paymentDate;
    private LocalDate lastPaymentDate;
    private Double amount;
    private String comment;
    private UUID paymentMethodId;
    private Double amountGiven = 0.00;
    private Boolean closedDebt = false;
    private Double totalSumma;
}
