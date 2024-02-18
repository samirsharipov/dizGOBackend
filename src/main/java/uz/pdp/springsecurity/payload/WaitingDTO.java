package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WaitingDTO {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID branchId;

    private UUID customerId;

    @NotNull
    private String dollar;

    @NotNull
    private String gross;

    @NotNull
    private double totalSum;

    @NotNull
    private double quantity;

    private String description;

    @NotNull
    private List<WaitingProductDto> waitingProductDtoList;
}
