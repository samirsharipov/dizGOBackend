package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class
ExchangeProductBranchDTO {

    private UUID shippedBranchId;

    private UUID receivedBranchId;

    private Date exchangeDate;

    private String description;

    private UUID exchangeStatusId;

    private List<ExchangeProductDTO> exchangeProductDTOS;

    private UUID businessId;

    private UUID userId;
}
