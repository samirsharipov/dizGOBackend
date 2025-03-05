package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.dizgo.erp.entity.Purchase;
import uz.dizgo.erp.entity.PurchaseProduct;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseGetOneDto {
    private Purchase purchase;
    private List<PurchaseProduct> purchaseProductList;
}
