package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReports {
    private String productName;
    private String branchName;
    private Integer quantity;
    private String categoryName;
    private Integer kpi;
    private Timestamp createDate;
    private Double tsPrice;
}
