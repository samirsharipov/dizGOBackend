package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionGetDto {
    private UUID id;
    private String businessName;
    private String tariffName;
    private String statusTariff;
    private Timestamp startDay;
    private Timestamp endDay;
    private double tariffPrice;
    private String payType;
    private boolean active;
    private double totalSum;
}
