package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOutlayDto {
    private UUID id;

    private UUID categoryId;

    private String categoryName;

    private UUID purchaseId;

    private UUID businessId;

    private double totalPrice;
}
