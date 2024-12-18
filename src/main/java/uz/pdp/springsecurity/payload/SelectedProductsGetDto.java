package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedProductsGetDto {
    private UUID id;
    // for product
    private UUID productId;
    private String productName;
    private double productPrice;

    private UUID branchId;
}
