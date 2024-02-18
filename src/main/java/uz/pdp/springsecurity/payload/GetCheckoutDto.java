package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCheckoutDto {
    private Timestamp timestamp;
    private double totalTradeSum;
    private double totalOutlay;
    private double totalCash;
    private double totalDebt;
}
