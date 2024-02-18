package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAboutDto {
    private String name;

    private Date date;

    private String description;
    private String measurement;

    private double amount;

    public ProductAboutDto(Date date, String description, double amount) {
        this.date = date;
        this.description = description;
        this.amount = amount;
    }
}
