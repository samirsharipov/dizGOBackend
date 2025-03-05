package uz.dizgo.erp.payload.excel.customertradehistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelCustomerTradeHistory {
    private String customerName;
    private String date;
    private String id;
    private List<ExcelProductTrade> products = new ArrayList<>();
}
