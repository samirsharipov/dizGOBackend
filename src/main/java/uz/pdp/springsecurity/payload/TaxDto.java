package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxDto {

    @NotNull(message = "required line")
    private String name;

    @NotNull(message = "required line")
    private Double percent;

    private Boolean active;

    private UUID businessId;
}
