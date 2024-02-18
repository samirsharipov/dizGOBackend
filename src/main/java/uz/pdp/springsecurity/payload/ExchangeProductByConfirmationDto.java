package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeProductByConfirmationDto {
    private UUID id;

    private ExchangeProductBranchDTO exchangeProductBranchDTO;

    private UUID carId;

    private Boolean confirmation;

    private String message;

    private String shippedBranchName;

    private String receivedBranchName;

    private UUID userBranchId;
}
