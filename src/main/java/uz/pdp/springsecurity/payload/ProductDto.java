package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @NotNull
    private String name;

    private Double minQuantity;

    private String barcode;

    private UUID brandId;

    private UUID categoryId;

    private UUID childCategoryId;

    @NotNull
    private UUID measurementId;

    private UUID photoId;

    @NotNull
    private double buyPrice;

    private double salePrice;
    private double grossPrice;
    private boolean buyDollar;
    private boolean saleDollar;
    private boolean grossDollar;
    private Integer grossPriceControl;
    private boolean kpiPercent;
    private double kpi;


    private double profitPercent;

    private double tax;

    @NotNull
    private List<UUID> branchId;

    private Date expireDate;

    private Date dueDate;

    @NotNull
    private UUID businessId;

    // types { SINGLE, MANY, COMBO }

    @NotNull
    private String type;

    //  fields for MANY types

    private List<ProductTypePricePostDto> productTypePricePostDtoList;

    //combo
    private List<ProductTypeComboDto> productTypeComboDtoList;
private Integer warehouseCount;
}
