package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetForPurchaseDto {

    private UUID productId;

    private String type;

    private String name;

    private String barcode;

    private double buyPrice;
    private double salePrice;
    private double buyPriceDollar;
    private boolean buyDollar;
    private double salePriceDollar;
    private boolean saleDollar;
    private double grossPrice;
    private double grossPriceDollar;
    private double grossPriceMyControl;
    private boolean grossPricePermission;

    private double amount;

    private double profitPercent;

    private String measurementName;
    private String subMeasurementName;
    private double subMeasurementValue;

    private String brandName;

    private Date expiredDate;

    private double minQuantity;

    private UUID photoId;
    private Boolean BusinessCheapSellingPrice;
    private UUID categoryId;
}
