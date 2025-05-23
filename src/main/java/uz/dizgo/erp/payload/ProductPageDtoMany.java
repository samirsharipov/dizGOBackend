package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageDtoMany {
    private String name;
    private String measurement;

    private double price;

    private UUID photo;

    private double amount;

    private double soldAmount;

    private double profit;
}
