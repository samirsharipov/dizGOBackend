package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewDtos {

    private String productName;
    private String branch;
    private double buyPrice;
    private double salePrice;
    private double amount;
    private String brandName;
    private double minQuantity;
    private Date expiredDate;
    private String barcode;
    private String measurementId;
}
