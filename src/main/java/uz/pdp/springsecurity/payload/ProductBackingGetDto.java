package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBackingGetDto {
    private Date date;
    private String CustomerName;
    private double quantity;

    public ProductBackingGetDto(Date date, double quantity) {
        this.date = date;
        this.quantity = quantity;
    }
}
