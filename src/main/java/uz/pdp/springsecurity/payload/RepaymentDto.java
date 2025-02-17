package uz.pdp.springsecurity.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
public class RepaymentDto {
    @NotNull
    private Double totalPaidSum;

    private Timestamp payDate;

    private String description;

    private UUID paymentMethodId;

    private UUID branchId;

    private UUID userId;

    List<UUID> purchaseIdList;
}
