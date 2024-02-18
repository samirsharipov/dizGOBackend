package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeProductDTO {

    @NotNull(message = "required line")
    private Double exchangeProductQuantity;

    private UUID productExchangeId;

    private UUID productTypePriceId;

    private String productName;

    private String productTypePriceName;

    private String measurementProductName;

    private String measurementProductTypePriceName;
}
