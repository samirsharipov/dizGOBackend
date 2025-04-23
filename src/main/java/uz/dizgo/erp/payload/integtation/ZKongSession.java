package uz.dizgo.erp.payload.integtation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZKongSession {
    private String token;
    private Instant expiry;
    private Long merchantId;
    private Long agencyId;
}

