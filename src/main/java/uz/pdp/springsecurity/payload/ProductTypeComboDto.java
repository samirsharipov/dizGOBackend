package uz.pdp.springsecurity.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeComboDto {
    private UUID comboId;
    private UUID mainProductId;
    private UUID contentProductId;
    private double amount;
    private double buyPrice;
    private double salePrice;
}
