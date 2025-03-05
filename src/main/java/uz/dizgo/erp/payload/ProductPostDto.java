package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPostDto {

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

    private String MXIKCode;

    private UUID businessId;            // Business reference

    private String agreementExportsID;  // Export agreement ID
    private String agreementExportsPID; // Export agreement PID
    private String agreementLocalID;    // Local agreement ID
    private String agreementLocalPID;   // Local agreement PID
    private String hsCode12;            // Harmonized System codes
    private String hsCode22;
    private String hsCode32;
    private String hsCode44;
    private String uniqueSKU;           // Unique Stock Keeping Unit
    private Double length;              // Product length
    private Double width;               // Product width
    private Double height;              // Product height
    private Double weight;              // Product weight
    private String shippingClass;       // Shipping classification

    // Main uchun kerak bo'lgan malumotlar
    private List<UUID> branchIds;       // Branches where the product is available


    // foydalanuvchi uchun kerak bo'lgan malumotlar
    private Boolean kpiPercent;         // KPI as percentage
    private Double kpi;                 // KPI value
    private double minQuantity;         // Minimum quantity for sale


    private Boolean isGlobal;   // Global availability

    private boolean clone;
}
