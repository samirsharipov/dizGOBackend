package uz.dizgo.erp.payload.integtation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZKongAddProductDto {
    private String itemBarcode;
    private String eslBarcode;
    private Long storeId;
    private String apMac;
}

