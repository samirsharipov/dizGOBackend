package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistoryGetDto{
    private String name;
    private String barcode;
    private String measurement;
    Date date;
    private double amount;
    private double plusAmount;
    private double minusAmount;
}
