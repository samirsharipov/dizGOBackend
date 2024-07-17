package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Language;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    private UUID subChildCategoryId;

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


    private String uniqueSKU;//unique SKU
    private String language;//language ru,uz,en
    private Double stockAmount;// stock amount
    private Boolean inStock; // in stock (stockda mavjudmi?)
    private Boolean preorder; // Preorder(oldindan buyurtma?)
    private Double length; // uzunligi
    private Double width; // // kengligi
    private Double height; // balandigi
    private Double weight; // og'irligi
    private String hsCode12;// frontend dan numeric kelishi kere
    private String hsCode22;//frontend dan numeric kelishi kere
    private String hsCode32;//frontend dan numeric kelishi kere
    private String hsCode44;//frontend dan numeric kelishi kere
    private String keyWord;
    private String briefDescription;//qisqa eslatma
    private String longDescription;//eslatma
    private String agreementExportsID;
    private String agreementExportsPID;
    private String agreementLocalID;
    private String agreementLocalPID;
    private String langGroup;
    private String shippingClass;
    private String attributes;
    private Boolean soldIndividualy;
    private boolean isGlobal;
}
