package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeGetDto {
    private UUID productTypeId;
    private String name;
    private UUID businessId;
    private List<ProductTypeValueDto> values;
}
