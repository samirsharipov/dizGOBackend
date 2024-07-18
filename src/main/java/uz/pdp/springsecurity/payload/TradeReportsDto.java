package uz.pdp.springsecurity.payload;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeReportsDto {
    private UUID tradeProductId;
    private String name;
    private String barcode;
    private String customerName;
    private Date tradedDate;
    private double amount;
    private double salePrice;
    private double discount;
    private double totalSum;
    private String payMethod;
    private Double profit;

}
