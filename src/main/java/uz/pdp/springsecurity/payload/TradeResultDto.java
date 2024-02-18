package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeResultDto {
    private Integer totalTrade;
    private Double totalTradeSumma;
    private double avarage;

    public double getAvarage() {
        return totalTradeSumma/totalTrade;
    }
}
