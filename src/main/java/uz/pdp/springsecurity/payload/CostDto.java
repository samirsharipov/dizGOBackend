package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostDto {
    @NotNull
    private UUID costTypeId;

    @NotNull
    private double sum;
}
