package uz.pdp.springsecurity.hr.payload;

import lombok.Data;
import uz.pdp.springsecurity.enums.Type;

import java.util.UUID;

@Data
public class AgentPlanProduct {
    private String label;
    private Type type;
    private UUID value;
}
