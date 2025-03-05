package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDto {
    @NotNull(message = "required")
    private double salary;

    private String description;

    private UUID paymentMethodId;
}
