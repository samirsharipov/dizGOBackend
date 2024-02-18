package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPostDto {
    private UUID businessId;

    private UUID tariffId;

    private String statusTariff;

    private String payType;

    private boolean activeNewTariff;
}
