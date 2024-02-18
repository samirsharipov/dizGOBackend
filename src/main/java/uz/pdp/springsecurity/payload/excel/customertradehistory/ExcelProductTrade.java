package uz.pdp.springsecurity.payload.excel.customertradehistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelProductTrade {
    private String name;
    private String barCode;
    private Double count;
    private Double perSum;
}
