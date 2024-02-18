package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Warehouse;
import uz.pdp.springsecurity.entity.WarehouseRasta;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewDto {

    private UUID productId;
    private String productName;
    private List<Branch> branch;
    private List<WarehouseRasta> rastas;
    private double buyPrice;
    private double salePrice;
    private String measurementId;
    private String subMeasurementName;
    private double subMeasurementValue;
    private String barcode;
    private UUID photoId;
    private double amount;
    private String brandName;
    private double minQuantity;
    private Date expiredDate;
    private String category;
    private double buyPriceDollar;
    private boolean buyDollar;
    private double salePriceDollar;
    private boolean saleDollar;
    private double grossPrice;
    private double grossPriceDollar;
    private double grossPriceControl;
    private boolean grossPricePermission;
    private Double kpi;
}
