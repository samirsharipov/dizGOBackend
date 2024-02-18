package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentProductDto{
    private UUID productId;

    //USE FOR MANY TYPE// OR NULL

    private UUID productTypePriceId;

    @NotNull
    private double quantity;

    @NotNull
    private double totalPrice;

    @NotNull
    private boolean byProduct;
}
