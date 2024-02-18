package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossProductGetDto {
    private String name;
    private String measurement;
    private Double lastAmount;
    private Double buyPrice;
    private String status;
    private double quantity;
}
