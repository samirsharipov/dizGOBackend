package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductDto {
    //USE FOR EDIT OR NULL
    private UUID purchaseProductId;

    //USE FOR SINGLE TYPE
    private UUID productId;

    //USE FOR MANY TYPE
    private UUID productTypePriceId;

    @NotNull(message = "required line")
    private Double purchasedQuantity;

    @NotNull(message = "required line")
    private double buyPrice;

    @NotNull(message = "required line")
    private double salePrice;

    private double totalSum;

    private boolean delete = false;
}
