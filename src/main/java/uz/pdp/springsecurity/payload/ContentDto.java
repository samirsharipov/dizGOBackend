package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto{
    private UUID branchId;

    private UUID productId;

    private UUID productTypePriceId;

    @NotNull
    private double quantity = 1;

    private boolean costEachOne;

    @NotNull
    private double contentPrice;


    @NotNull
    private double totalPrice;

    private double cost;

    private List<CostDto> costDtoList;

    @NotNull
    List<ContentProductDto> contentProductDtoList;
}
