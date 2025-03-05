package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutlayDto {
    @NotNull(message = "required line")
    private UUID outlayCategoryId;
    @NotNull(message = "required line")
    private double totalSum;
    @NotNull(message = "required line")
    private UUID branchId;
    @NotNull(message = "required line")
    private UUID spenderId;
    @NotNull(message = "required line")
    private String description;
    private Date date;
    private UUID paymentMethodId;
    private Boolean dollarOutlay;
}
