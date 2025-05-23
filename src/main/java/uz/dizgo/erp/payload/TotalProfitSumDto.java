package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalProfitSumDto {
    private double summa;
    private double percentage;
    private String message;
    private boolean success;
}
