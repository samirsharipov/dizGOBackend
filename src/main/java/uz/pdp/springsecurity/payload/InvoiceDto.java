package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto {
    @NotNull(message = "REQUIRED")
    private String name;

    private String description;

    private UUID photoId;

    @NotNull(message = "REQUIRED")
    private String footer;
}
