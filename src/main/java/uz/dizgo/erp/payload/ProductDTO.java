package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private UUID id;                    // Unique product identifier
    private String name;                // Product name
    private String description;         // Short description
    private String longDescription;     // Long description
    private String keywords;            // Keywords for search
    private String attributes;          // Additional attributes

    private String uniqueSKU;           // Unique Stock Keeping Unit
    private double salePrice;           // Sale price in sum
    private double salePriceDollar;     // Sale price in dollar

    private Double stockAmount;         // Available stock amount
    private Boolean inStock;            // In stock status
    private Boolean preorder;           // Preorder status
    private Double length;              // Product length
    private Double width;               // Product width
    private Double height;              // Product height
    private Double weight;              // Product weight

    private String MXIKCode;

    private String agreementExportsID;  // Export agreement ID
    private String agreementExportsPID; // Export agreement PID
    private String agreementLocalID;    // Local agreement ID
    private String agreementLocalPID;   // Local agreement PID
    private String langGroup;           // Language group identifier
    private String shippingClass;       // Shipping classification
    private Boolean soldIndividually;   // Individual sale status

    private Date dueDate;               // Due date
    private boolean active;             // Active status
    private double profitPercent;       // Profit percentage
    private double tax;                 // Tax percentage
    private double buyPrice;            // Buying price

    private double grossPrice;          // Gross price
    private double grossPriceDollar;    // Gross price in dollars
    private int grossPriceMyControl;    // Custom gross price

    private double buyPriceDollar;      // Buying price in dollars
    private boolean buyDollar;          // Buy with dollar status
    private boolean saleDollar;         // Sale with dollar status

    private Boolean kpiPercent;         // KPI as percentage
    private Double kpi;                 // KPI value
    private Date expireDate;            // Expiry date

    private String barcode;             // Product barcode
    private String pluCode;

    private double minQuantity;

    private String brand;
    private String category;
    private String measurement;
    private UUID photo;
    private String business;
}