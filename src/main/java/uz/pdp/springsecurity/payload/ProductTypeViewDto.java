package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeViewDto {

    private String name;
    private double buyPrice;
    private double salePrice;
    private double amount;
    private String barcode;
    private UUID photoId;
}
