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
    private Double salePrice;
    private String customerName;
    private String payMethod;
    private Double amount;
    private Double discount;
    private Double totalSum;
    private Double profit;
    private Date tradedDate;
    private String invoice;
    private String traderName;

    // getters and setters
}