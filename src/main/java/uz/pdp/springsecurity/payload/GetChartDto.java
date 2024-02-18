package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetChartDto {
    private Timestamp timestamp;
    private double totalPurchase;
    private double totalDebt;
    private double totalMyDebt;
    private double totalTrade;
}
