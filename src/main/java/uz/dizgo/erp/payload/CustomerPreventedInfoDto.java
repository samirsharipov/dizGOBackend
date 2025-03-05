package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPreventedInfoDto {
    private TotalPaidSumDto totalPaidSumDto;
    private BackingProductDto backingProductDto;
    private CustomerDebtRepaymentDto customerDebtRepaymentDto;
    private double debtSum = 0;
    private double balance;
    private String description;
}
