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
public class PrizeDto {
    @NotNull(message = "REQUIRED")
    private UUID branchId;

    @NotNull(message = "REQUIRED")
    private UUID bonusId;

    @NotNull(message = "REQUIRED")
    private UUID userId;

    @NotNull(message = "REQUIRED")
    private Date date;

    @NotNull(message = "REQUIRED")
    private boolean given;

    private String description;

    private Integer count;
    private boolean task;
    private boolean lid;
    private Date deadline;
}
