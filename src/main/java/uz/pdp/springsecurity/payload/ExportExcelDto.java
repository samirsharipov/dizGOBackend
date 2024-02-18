package uz.pdp.springsecurity.payload;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportExcelDto {

    private String productName;
    private double buyPrice;
    private double salePrice;
    private double amount;
    private double minQuantity;
    private Date expiredDate;
    private String barcode;
}
