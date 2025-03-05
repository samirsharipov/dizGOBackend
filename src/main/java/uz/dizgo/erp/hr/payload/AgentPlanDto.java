package uz.dizgo.erp.hr.payload;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AgentPlanDto {
    private UUID branch;
    private Date startDate;
    private Date endDate;
    private Integer plan;
    private UUID user;
    private AgentPlanProduct product;
}
