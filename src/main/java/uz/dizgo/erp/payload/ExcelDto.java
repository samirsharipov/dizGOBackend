package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelDto {

    private String name;
    private double buyPrice;
    private double salePrice;
    private double wholeSale;
    private String dollarBuy;
    private String dollarSale;
    private double amount;
    private double alertQuantity;
    private String measurement;
    private String typeSize;
    private String typeColor;
    private String brand;
    private String category;
    private String barcode;
    private Date expiredDate;
    private String photo;
}
