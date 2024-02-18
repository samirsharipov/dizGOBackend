package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private UUID id;
    @NotNull
    private String name;
    private Date startDate;
    private Date endDate;
    private Date deadline;
    private UUID projectTypeId;
    @NotNull
    private UUID customerId;
    private String description;
    private List<String> stages;
    private List<UUID> userList;
    private List<UUID> fileDateList;
    private double budget;
    private double goalAmount;
    private boolean isProduction;
    @NotNull
    private UUID branchId;
}
