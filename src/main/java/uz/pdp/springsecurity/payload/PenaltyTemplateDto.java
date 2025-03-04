package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PenaltyTemplateDto {
    private String name; // Jarima shablonining nomi, masalan, "Kechikish jarimasi"

    private UUID branchId; // Qaysi filialga tegishli ekanligi

    private BigDecimal perMinutePenalty; // Har daqiqa uchun jarima summasi
}
