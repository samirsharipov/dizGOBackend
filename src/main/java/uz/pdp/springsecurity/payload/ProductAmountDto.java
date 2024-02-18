package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAmountDto {
    private String name;
    private String measurement;
    private double average;
    private double day;
    private double month;
    private double amount;
}
