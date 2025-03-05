package uz.dizgo.erp.payload;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffBusinessInfoDto {

    // Getter va setter larini qo'shish
    @Setter
    @Getter
    private String tariffName;  // Tarif nomi qo'shiladi


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
