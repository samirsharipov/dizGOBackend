package uz.pdp.springsecurity.payload;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class GeneralPlanDto {
    private Integer tam;
    private Integer sam;
    private Integer som;
    private Integer agentsCount;
    private Date startDate;
    private Date endDate;
    private UUID branch;
}
