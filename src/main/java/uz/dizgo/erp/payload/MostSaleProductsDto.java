package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostSaleProductsDto {

    private String name;
    private String barcode;
    private double salePrice;
    private double buyPrice;
    private String branchName;
    private double amount;
    private String measurement;
    private UUID attachmentId;
}
