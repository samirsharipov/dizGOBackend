package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingGetDto {
    private UUID id;

    private UUID userId;

    private UUID customerId;

    private String dollar;

    private String gross;

    private double totalSum;

    private double quantity;

    private String description;

    List<WaitingProductGetDto> waitingProductGetDtoList;
}
