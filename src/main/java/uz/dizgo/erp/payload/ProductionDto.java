package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionDto{
    private UUID branchId;

    private UUID productId;

    private UUID productTypePriceId;

    private Date date;

    @NotNull
    private double totalQuantity;

    private double invalid;

    @NotNull
    private double contentPrice;

    @NotNull
    private double totalPrice;

    private boolean costEachOne;

    private double cost;

    private List<CostDto> costDtoList;
    private String  description;

    private String selectedSmen;




    @NotNull
    @NotEmpty
    List<ContentProductDto> contentProductDtoList;
    List<ContentProductDto> lossContentProductDtoList;


}
