package uz.pdp.springsecurity.shoxjaxon.activity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductAbout2 {

    private UUID id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double amount;
    private String description;
    private UUID tradeId;
    private UUID branchId;
    private UUID productId;
    private UUID productTypePriceId;

    // Qo'shilgan maydonlar
    private String branchName;
    private String productName;

    private String productTypeName;  // Yangi ustun
    private String measurementName;  // Yangi qo'shilgan maydon



    // Default constructor
    public ProductAbout2() {
    }

    // Parameterized constructor
    public ProductAbout2(UUID id, LocalDateTime createdAt, LocalDateTime updatedAt, Double amount,
                         String description, UUID tradeId, UUID branchId, UUID productId, UUID productTypePriceId,
                         String branchName, String productName, String productTypeName ) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.amount = amount;
        this.description = description;
        this.tradeId = tradeId;
        this.branchId = branchId;
        this.productId = productId;
        this.productTypePriceId = productTypePriceId;
        this.branchName = branchName;
        this.productName = productName;
        this.productTypeName =  productTypeName;
    }
}
