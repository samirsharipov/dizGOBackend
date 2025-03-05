package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.entity.WarehouseRasta;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductViewDto {

    private UUID productId;
    private String productName;
    private List<Branch> branch;
    private List<WarehouseRasta> rastas;
    private double buyPrice;
    private double salePrice;
    private String measurementId;
    private String subMeasurementName;
    private double subMeasurementValue;
    private String barcode;
    private UUID photoId;
    private double amount;
    private String brandName;
    private double minQuantity;
    private Date expiredDate;
    private String category;
    private String childCategory;
    private String subChildCategory;
    private double buyPriceDollar;
    private boolean buyDollar;
    private double salePriceDollar;
    private boolean saleDollar;
    private double grossPrice;
    private double grossPriceDollar;
    private double grossPriceControl;
    private boolean grossPricePermission;
    private Double kpi;


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
}
