package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDto {
    private String productName;
    private Integer amount;
    private String branchName;
    private Timestamp time;
    private Integer kpi;
}