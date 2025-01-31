package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedProductsGetDto {
    private UUID selectedProductId;
    // for product
    private UUID id;
    private String name;
    private double salePrice;
    private String barcode;
    private String MXIKCode;
    private Boolean discount;

    private UUID branchId;

    // for discount
    private Double discountValue;
    private Boolean percentage;
}
