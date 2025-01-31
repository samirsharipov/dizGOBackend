package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private double salePrice;
    private String barcode;
    private String MXIKCode;
    private Boolean discount;
    private Double amount;

    public ProductResponseDTO(UUID id, String name, double salePrice, String barcode, String MXIKCode, Boolean discount) {
        this.id = id;
        this.name = name;
        this.salePrice = salePrice;
        this.barcode = barcode;
        this.MXIKCode = MXIKCode;
        this.discount = discount;
    }

    // for discount
    private Double discountValue;
    private Boolean percentage;
}
