package uz.dizgo.erp.hr.payload;

import lombok.Data;
import uz.dizgo.erp.enums.Type;

import java.util.UUID;

@Data
public class AgentPlanProduct {
    private String label;
    private Type type;
    private UUID value;
}
