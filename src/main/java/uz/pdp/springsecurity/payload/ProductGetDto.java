package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetDto {
    private UUID id;                 // Mahsulotning unikal identifikatori
    private Timestamp createdAt;          // Yaratilgan sana va vaqt

    // Mahsulotning asosiy ma'lumotlari
    private String name;
    private String description;
    private String longDescription;
    private String keywords;
    private String attributes;

    // Narx va inventar ma'lumotlari
    private String uniqueSKU;
    private double salePrice;
    private double salePriceDollar;
    private Double stockAmount;
    private Boolean inStock;
    private Boolean preorder;
    private Double length;
    private Double width;
    private Double height;
    private Double weight;

    // Harmonizatsiya kodlari
    private String hsCode12;
    private String hsCode22;
    private String hsCode32;
    private String hsCode44;

    // Shartnomalar
    private String agreementExportsID;
    private String agreementExportsPID;
    private String agreementLocalID;
    private String agreementLocalPID;
    private String langGroup;
    private String shippingClass;
    private Boolean soldIndividually;

    // Qo'shimcha ma'lumotlar
    private Date dueDate;
    private boolean active;
    private double profitPercent;
    private double tax;
    private double buyPrice;
    private double grossPrice;
    private double grossPriceDollar;
    private int grossPriceMyControl;
    private double buyPriceDollar;
    private boolean buyDollar;
    private boolean saleDollar;
    private Boolean kpiPercent;
    private Double kpi;
    private Date expireDate;
    private String barcode;
    private String pluCode;
    private double minQuantity;

    // Bog'lanishlar
    private String brandName;
    private UUID brandId;
    private String categoryName;
    private UUID categoryId;
    private String measurementUnit;
    private UUID measurementUnitId;
    private String businessName;
    private List<String> branches;
    private List<UUID> branchIds;
    private List<String> rastaList;
    private UUID photoId;

    // Qo'shimcha maydonlar
    private double warehouseCount;
    private double quantity;
    private Boolean isGlobal;
    private boolean main;

    private String mxikCode;

    List<ProductTranslateDTO> translates;
}
