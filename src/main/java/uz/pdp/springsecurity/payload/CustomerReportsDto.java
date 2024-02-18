package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerReportsDto {
    private String customerName;
    private String product;
    private Date date;
    private String branchName;
    private String paymentStatus;
    private double totalSum;
    private double paidSum;
    private double debt;
    private double tradedQuantity;
    private String payMethod;
}
