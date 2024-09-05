package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffBusinessInfoDto {
    private long tradeAmount;
    private long yourTradeAmount;

    private int employeeAmount;
    private int yourEmployeeAmount;

    private int branchAmount;
    private int yourBranchAmount;

    private long productAmount;
    private long yourProductAmount;

    private long testDay;
    private long yourTestDay;

    private long intervalDay;
    private long remainingDays;
}
