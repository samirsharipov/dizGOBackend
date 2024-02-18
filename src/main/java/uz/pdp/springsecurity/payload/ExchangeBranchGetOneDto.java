package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeBranchGetOneDto {

    private String productName;
    private String barCode;

    private double exchangeProductQuantity;
}
