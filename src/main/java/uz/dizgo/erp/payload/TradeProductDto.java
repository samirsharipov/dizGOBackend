package uz.dizgo.erp.payload;

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

    private UUID productId;

    private String productName;

    private String type;

    @NotNull(message = "required line")
    private double tradedQuantity;

    private double totalSalePrice;

    private boolean delete = false;

    private boolean subMeasurement;

}
