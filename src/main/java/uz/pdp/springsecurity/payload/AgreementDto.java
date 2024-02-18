package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDto {
    private UUID id;
    @NotNull
    private String salaryStatus;

    @NotNull
    private double price = 0;

    @NotNull
    private boolean active;
}
