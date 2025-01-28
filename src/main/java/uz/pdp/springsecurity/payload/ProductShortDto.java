package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductShortDto {
    private UUID id;
    private String name;
    private UUID brandId;
    private UUID categoryId;
    private double salePrice;
}
