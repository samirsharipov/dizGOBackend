package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Product;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetDto {
    private Product product;

    private List<ProductTypePriceGetDto> productTypePriceGetDtoList;

    private List<ProductTypeComboGetDto> comboGetDtoList;

    public ProductGetDto(Product product) {
        this.product = product;
    }

}
