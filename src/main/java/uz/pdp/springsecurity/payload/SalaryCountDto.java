package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryCountDto {
    @NotNull(message = "required")
    private double count;

    @NotNull(message = "required")
    private double salary;

    @NotNull(message = "required")
    private UUID agreementId;

    @NotNull(message = "required")
    private UUID branchId;

    @NotNull(message = "required")
    private Date date;

    private String description;
}
