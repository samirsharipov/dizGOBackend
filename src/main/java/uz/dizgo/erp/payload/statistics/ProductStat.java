package uz.dizgo.erp.payload.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStat {
    private long totalProducts;
    private long cloneProducts;
    private long globalFalse;
}
