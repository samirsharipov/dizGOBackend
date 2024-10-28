package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Product;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeComboGetDto {
    private UUID comboId;

    private Product contentProduct;

    private double amount;

    private double buyPrice;

    private double salePrice;
}
