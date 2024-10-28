package uz.pdp.springsecurity.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Type;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResult {
    private UUID id;
    private String name;
    private Double amount;
    private Double percentage;
    private List<ProductResult> products = new LinkedList<>();
}
