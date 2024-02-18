package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroupDto {
    private UUID id;
    
    @NotNull(message = "required line")
    private String name;

    @NotNull(message = "required line")
    private Double percent;

    private UUID businessId;
}
