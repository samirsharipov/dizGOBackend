package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutlayTrade {
    private double outLayOne;
    private double outLayTwo;
    private double outLayTree;
    private double outLayFour;
    private double outLayFive;
    private double tradeOne;
    private double tradeTwo;
    private double tradeTree;
    private double tradeFour;
    private double tradeFive;
}
