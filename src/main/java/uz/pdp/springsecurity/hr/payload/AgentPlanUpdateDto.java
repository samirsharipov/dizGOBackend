package uz.pdp.springsecurity.hr.payload;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class AgentPlanUpdateDto {
    private Date startDate;
    private Date endDate;
    private Integer plan;
    private UUID user;
}
