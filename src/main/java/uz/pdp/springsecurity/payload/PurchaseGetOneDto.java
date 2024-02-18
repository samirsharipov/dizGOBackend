package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Purchase;
import uz.pdp.springsecurity.entity.PurchaseProduct;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseGetOneDto {
    private Purchase purchase;
    private List<PurchaseProduct> purchaseProductList;
}
