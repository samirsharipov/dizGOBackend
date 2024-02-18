package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseProductGetDto {
    private String name;
    private String measurement;
    private double quantity;
    private double buyPrice;
    private double salePrice;
    private double totalSum;
    private double soldQuantity;
    private double profit;

    public PurchaseProductGetDto(double quantity, double buyPrice, double salePrice, double totalSum) {
        this.quantity = quantity;
        this.buyPrice = buyPrice;
        this.salePrice = salePrice;
        this.totalSum = totalSum;
    }
}
