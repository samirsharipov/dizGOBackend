package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDto {
    private double average;
    private double day;
    private double week;
    private double month;

    private double amount;
    private double soldAmount;
    private double purchaseAmount;
    private double returnAmount;

    private double buyPrice;
    private double salePrice;
    private double soldPrice;
    private double profit;
}
