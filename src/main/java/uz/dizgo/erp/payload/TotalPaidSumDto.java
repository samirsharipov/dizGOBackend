package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalPaidSumDto {
    private Timestamp createAt;
    private Double paidSum;
    private String payMethodName;
    private String description;
}
