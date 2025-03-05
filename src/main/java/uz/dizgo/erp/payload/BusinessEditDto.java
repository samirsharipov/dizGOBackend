package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessEditDto {
    private UUID id;
    @NotNull(message = "required line")
    private String name;

    private String description;

    private boolean active;

    private boolean deleted ;

    private String businessNumber;

    private String status;

    private Timestamp contractStartDate;
    private Timestamp contractEndDate;


}
