package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEditDto {
    private List<ProductTranslateDTO> translations;

    private String name;                // Product name
    private String description;         // Short description
    private String longDescription;     // Long description
    private String keywords;            // Keywords for search
    private String attributes;          // Additional attributes


    private String pluCode;             // Product pluCode
    private String barcode;             // Product barcode

    private UUID measurementId;         // Measurement reference
    private UUID brandId;               // Brand reference
    private UUID categoryId;            // Category reference
    private UUID photoId;               // Attachment/photo reference

    private String MXIKCode;            // soliq uchun mxik kodi

    private double buyPrice;            // Mahsulotning olish narxi (so'm)
    private double salePrice;           // Mahsulotning sotish narxi (so'm)
    private double grossPrice;          // Optom narxi

    private double kpi;
    private double minQuantity;

}
