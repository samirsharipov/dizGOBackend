package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeProductCustomerDto {
    private String productName;
    private UUID attachmentId;
    private Double productCount;
    private String measurementName;
}
