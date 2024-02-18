package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayMethodDto {
    @NotNull(message = "required line")
    private String type;

    @NotNull(message = "required line")
    private UUID businessId;
}
