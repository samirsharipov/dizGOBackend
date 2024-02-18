package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    @NotNull(message = "required line")
    private UUID supplerId;
    @NotNull(message = "required line")
    private UUID seller;
    @NotNull(message = "required line")
    private UUID purchaseStatusId;
    @NotNull(message = "required line")
    private UUID paymentStatusId;
    @NotNull(message = "required line")
    private UUID branchId;
    private UUID paymentMethodId;
    @NotNull(message = "required line")
    private Date date;

    private String description;

    private double deliveryPrice;

    private double totalSum;

    private double debtSum;

    private Double paidSum;

    @NotNull(message = "required line")
    private List<PurchaseProductDto> purchaseProductsDto;
}
