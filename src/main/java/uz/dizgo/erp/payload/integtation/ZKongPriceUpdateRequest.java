package uz.dizgo.erp.payload.integtation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZKongPriceUpdateRequest {
    private String barcode;
    private Double price;
    private Long storeId;
}
