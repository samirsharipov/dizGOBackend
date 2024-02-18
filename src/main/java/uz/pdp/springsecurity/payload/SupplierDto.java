package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {

    @NotNull(message = "required line")
    private String name;
    @NotNull(message = "required line")
    private String phoneNumber;
    @NotNull(message = "required line")
    private String telegram;
    @NotNull(message = "required line")
    private boolean juridical;

    private String inn;

    private String companyName;

    private UUID businessId;

    private double debt;

}
