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
public class NavigationDto {
    @NotNull
    private UUID branchId;

    @NotNull
    private double initial;

    @NotNull
    private double goal;

    private Date startDate;

    @NotNull
    private Date endDate;

    private String description;
}
