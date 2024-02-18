package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingProductDto {
    @NotNull
    private UUID productId;

    @NotNull
    private double quantity;

    @NotNull
    private double totalPrice;

    @NotNull
    private double salePrice;

    @NotNull
    private boolean subMeasurement;
}
