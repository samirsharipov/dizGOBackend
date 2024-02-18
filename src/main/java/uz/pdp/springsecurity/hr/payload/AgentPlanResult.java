package uz.pdp.springsecurity.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentPlanResult {
    private UUID id;
    private Integer plan;
    private Date startDate;
    private Date endDate;
    private AgentPlanProductResult product;
    private AgentPlanUserResult user;
    private Integer sale;
    private UUID branchId;
}
