package uz.dizgo.erp.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralPlanResult {
    private UUID id;
    private Integer tam;
    private Integer sam;
    private Integer som;
    private Integer agentsCount;
    private Date startDate;
    private Date endDate;
    private boolean active;
    private Date date;
    private Object branch;
}
