package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductHistoryDto {
    private String productName;
    private double amount;
    private double summa;
    private Date date;
    private String userFirstName;
    private String userLastName;
    private String customerSupplierName;

    public ProductHistoryDto(String productName, double amount, double summa, Date date) {
        this.productName = productName;
        this.amount = amount;
        this.summa = summa;
        this.date = date;
    }
}
