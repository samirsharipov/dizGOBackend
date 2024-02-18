package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonusDto {
    @NotNull(message = "REQUIRED")
    private UUID businessId;

    @NotNull(message = "REQUIRED")
    private String name;

    @NotNull(message = "REQUIRED")
    private String color;

    @NotNull(message = "REQUIRED")
    private String icon;

    @NotNull(message = "REQUIRED")
    private double summa;
}
