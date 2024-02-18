package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeProductDto {
    private UUID tradeProductId;

    //USE FOR SINGLE TYPE// OR NULL
    private UUID productId;

    private String type;

    //USE FOR MANY TYPE// OR NULL
    private UUID productTypePriceId;

    @NotNull(message = "required line")
    private double tradedQuantity;

    private double totalSalePrice;

    private boolean delete = false;

    private boolean subMeasurement;
}
