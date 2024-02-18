package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypePricePostDto {

    private UUID productTypePriceId;

    @NotNull
    private UUID productTypeValueId;

    private UUID subProductTypeValueId;

    private UUID photoId;

    private String barcode;

    @NotNull(message = "required line")
    private double buyPrice;
    @NotNull
    private double salePrice;
    @NotNull
    private double grossPrice;
    @NotNull
    private double profitPercent;
    private Integer warehouseCount;
}
